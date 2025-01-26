package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchantments.other.FileFunction;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Trigger implements CustomEnchantListener {

    private static final Random RG = new Random();

    private static final Map<Player, Map<Enchantment, Long>> pendingCooldown = new HashMap<>();

    private static final Pattern delayCommandPattern = Pattern.compile("^[ ]*delay ([0-9]+)[ ]*$");

    private final CustomEnchant enchantment;

    private final EnchantTriggerType type;

    private List<String> commands;

    public Trigger(CustomEnchant customEnchant, EnchantTriggerType type){
        this.enchantment = customEnchant;
        this.type = type;
    }

    protected void executeCommands(Event e, Player player, String triggerCondition, Map<String, String> parameters){
        executeCommands(e, player, Collections.emptySet(), triggerCondition, parameters);
    }

    protected void executeCommands(Event e, Player player, String triggerCondition, Map<String, String> parameters, Runnable onComplete){
        executeCommands(e, player, Collections.emptySet(), triggerCondition, parameters, onComplete);
    }

    protected void executeCommands(Event e, Player player, Set<ItemStack> customEnchantedItemsSelection, String triggerCondition, Map<String, String> localParameters){
        executeCommands(e, player, customEnchantedItemsSelection, triggerCondition, localParameters, () -> {});
    }

    protected void executeCommands(Event e, Player player, Set<ItemStack> customEnchantedItemsSelection, String triggerCondition, Map<String, String> localParameters, Runnable onComplete){
        if (executeCommandsPreparation(e, player, customEnchantedItemsSelection, triggerCondition, localParameters)){
            dispatchCommand(0, onComplete);
        } else {
            onComplete.run();
        }
    }

    protected boolean executeCommandsPreparation(Event e, Player player, Set<ItemStack> customEnchantedItemsSelection, String triggerCondition, Map<String, String> localParameters){
        if (player == null)
            return false;
        PlayerInventory inv = player.getInventory();
        Location location = player.getLocation();

        ItemStack enchantedItem;
        if (customEnchantedItemsSelection.isEmpty())
            enchantedItem = Util.getEnchantedItem(inv, this.enchantment);
        else
            enchantedItem = Util.getEnchantedItem(customEnchantedItemsSelection, this.enchantment);

        if (enchantedItem == null)
            return false;

        //Get the level of the enchantment
        int level = Util.getLevel(enchantedItem, this.enchantment.getEnchantment());

        //Check if this enchantment is still in cooldown for the player
        pendingCooldown.computeIfAbsent(player, v -> new HashMap<>());
        if (pendingCooldown.get(player).containsKey(this.enchantment.getEnchantment())){
            if (enchantment.hasCoolDownMessage()){
                Long startTime = pendingCooldown.get(player).get(this.enchantment.getEnchantment());
                int timeLeftSeconds = enchantment.getCooldown(level) - (int)((System.currentTimeMillis() - startTime)/1000);
                player.sendMessage(Util.replaceParameters(Map.of(
                        "player", player.getDisplayName(),
                        "enchantment", enchantment.getName(),
                        "time_left", Util.secondsToString(timeLeftSeconds, false),
                        "time_left_full_out", Util.secondsToString(timeLeftSeconds, true)
                ), enchantment.getCoolDownMessage()));
            }
            return false;
        }

        //Check if the trigger conditions are met
        if (triggerCondition != null && !this.enchantment.checkTriggerConditions(triggerCondition, type))
            return false;


        //Return if chance didn't trigger
        if (RG.nextInt(10001) > enchantment.getChance(level))
            return false;


        //Cancel event if specified to do so
        if (e instanceof Cancellable event)
            event.setCancelled(enchantment.isCancelled(level));


        //Add cool down if necessary
        if (enchantment.getCooldown(level) > 0){
            pendingCooldown.get(player).put(this.enchantment.getEnchantment(), System.currentTimeMillis());
            Bukkit.getScheduler().runTaskLater(Main.getMain(), v -> pendingCooldown.get(player).remove(this.enchantment.getEnchantment()), enchantment.getCooldown(level) * 20L);
        }


        //add global parameters to parameter map
        HashMap<String, String> parameters = new HashMap<>(localParameters);
        parameters.put("player", player.getDisplayName());
        parameters.put("x", String.valueOf(location.getX()));
        parameters.put("y", String.valueOf(location.getY()));
        parameters.put("z", String.valueOf(location.getZ()));

        //Replace parameters
        commands = enchantment.getCommands(level).stream().map(command -> Util.replaceParameters(parameters, command)).collect(Collectors.toList());


        //Execute file functions
        try {
            commands = FileFunction.parse(commands);
        } catch (NumberFormatException exception){
            return false;
        }

        return true;
    }

    private void dispatchCommand(int index, Runnable onComplete){
        if (index == commands.size()) {
            onComplete.run();
            return;
        }
        String command = commands.get(index);
        Matcher matcher = delayCommandPattern.matcher(command);
        if (matcher.matches())
            Bukkit.getScheduler().runTaskLater(Main.getMain(), () -> dispatchCommand(index + 1, onComplete), Integer.parseInt(matcher.group(1)) * 20L);
        else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            dispatchCommand(index + 1, onComplete);
        }
    }


}
