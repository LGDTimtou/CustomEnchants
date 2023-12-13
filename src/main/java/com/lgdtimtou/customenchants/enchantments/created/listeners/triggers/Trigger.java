package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.Main;
import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchants.other.FileFunction;
import com.lgdtimtou.customenchants.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.stream.Collectors;

public class Trigger implements CustomEnchantListener {

    private static final Random RG = new Random();

    private static final Map<Player, Set<Enchantment>> pendingCooldown = new HashMap<>();

    private final CustomEnchant enchantment;

    private final EnchantTriggerType type;

    private int level;
    private List<String> commands;

    public Trigger(Enchantment enchantment, EnchantTriggerType type){
        this.enchantment = CustomEnchant.get(enchantment.getKey().getKey());
        this.type = type;
    }

    protected void executeCommands(Event e, Player player, String triggerCondition, Map<String, String> parameters){
        executeCommands(e, player, Collections.emptySet(), triggerCondition, parameters);
    }

    protected void executeCommands(Event e, Player player, Set<ItemStack> customEnchantedItemsSelection, String triggerCondition, Map<String, String> parameters){
        if (player == null)
            return;
        PlayerInventory inv = player.getInventory();
        Location location = player.getLocation();


        ItemStack enchantedItem = Util.getEnchantmentContainingItem(inv, customEnchantedItemsSelection, this.enchantment.getEnchantment());
        if (enchantedItem == null)
            return;

        //Check if this enchantment is still in cooldown for the player
        pendingCooldown.computeIfAbsent(player, v -> new HashSet<>());
        if (pendingCooldown.get(player).contains(this.enchantment.getEnchantment()))
            return;

        //Check if the trigger conditions are met
        if (triggerCondition != null && !this.enchantment.checkTriggerConditions(triggerCondition, type))
            return;
        //Get the level of the enchantment
        level = Util.getLevel(enchantedItem, this.enchantment.getEnchantment());

        //Return if chance didn't trigger
        if (RG.nextInt(10001) > enchantment.getChance(level))
            return;


        //Cancel event if specified to do so
        if (e instanceof Cancellable event)
            event.setCancelled(enchantment.isCancelled(level));


        //Replace global parameters
        //player's name
        commands = enchantment.getCommands(level).stream().map(command -> command.replace("%player%", player.getDisplayName())).collect(Collectors.toList());
        //Coordinates
        commands = commands.stream().map(c -> c.replace("%x%", String.valueOf(location.getX()))
                .replace("%y%", String.valueOf(location.getY())).replace("%z%", String.valueOf(location.getZ()))).collect(Collectors.toList());

        try {
            commands = FileFunction.parse(commands);
        } catch (NumberFormatException exception){
            return;
        }

        //Replace local parameters
        parameters.forEach((key, value) -> commands = commands.stream().map(command -> command.replace("%" + key + "%", value.toLowerCase())).collect(Collectors.toList()));

        //Execute the commands from the console
        dispatchCommands(player);
    }

    private void dispatchCommands(Player player){
        commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        if (enchantment.getCooldown(level) > 0){
            pendingCooldown.get(player).add(this.enchantment.getEnchantment());
            Bukkit.getScheduler().runTaskLater(Main.getMain(), v -> pendingCooldown.get(player).remove(this.enchantment.getEnchantment()), enchantment.getCooldown(level) * 20L);
        }
    }


}
