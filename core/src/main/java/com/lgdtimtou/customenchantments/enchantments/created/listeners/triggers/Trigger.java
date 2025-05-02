package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchantments.other.FileFunction;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trigger implements CustomEnchantListener {

    private static final Random RG = new Random();

    private static final Map<Player, Map<Enchantment, Long>> pendingCooldown = new HashMap<>();

    private static final Pattern delayCommandPattern = Pattern.compile("^[ ]*delay ([0-9]+)[ ]*$");

    private final CustomEnchant enchantment;

    private final EnchantTriggerType type;

    private List<String> commands;

    public Trigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        this.enchantment = customEnchant;
        this.type = type;
    }

    protected void executeCommands(Event e, Player player, Map<ConditionKey, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters) {
        executeCommands(e, player, Collections.emptySet(), triggerConditionMap, localParameters);
    }

    protected void executeCommands(Event e, Player player, Map<ConditionKey, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters, Runnable onComplete) {
        executeCommands(e, player, Collections.emptySet(), triggerConditionMap, localParameters, onComplete);
    }

    protected void executeCommands(Event e, Player player, Set<ItemStack> customEnchantedItemsSelection, Map<ConditionKey, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters) {
        executeCommands(e, player, customEnchantedItemsSelection, triggerConditionMap, localParameters, () -> {});
    }

    protected void executeCommands(Event e, Player player, Set<ItemStack> customEnchantedItemsSelection, Map<ConditionKey, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters, Runnable onComplete) {
        // Add all global conditions
        Map<ConditionKey, Object> mutableTriggerConditionMap = new HashMap<>(triggerConditionMap);
        mutableTriggerConditionMap.putAll(TriggerConditionType.getGlobalConditionMap(player));

        if (executeCommandsPreparation(e, player, customEnchantedItemsSelection, mutableTriggerConditionMap)) {
            // Add global parameters to parameter map
            HashMap<String, Supplier<String>> parameters = new HashMap<>(localParameters);
            parameters.put("player", player::getDisplayName);
            parameters.put("x", () -> String.valueOf(player.getLocation().getX()));
            parameters.put("y", () -> String.valueOf(player.getLocation().getY()));
            parameters.put("z", () -> String.valueOf(player.getLocation().getZ()));

            // Add condition type parameters to parameter map
            mutableTriggerConditionMap.forEach((conditionKey, obj) -> parameters.putAll(conditionKey.type()
                                                                                                    .getConditionParameters(
                                                                                                            conditionKey.prefix(),
                                                                                                            obj
                                                                                                    )));
            dispatchCommand(0, parameters, onComplete);
        } else {
            onComplete.run();
        }
    }

    protected boolean executeCommandsPreparation(Event e, Player player, Set<ItemStack> priorityItems, Map<ConditionKey, Object> triggerConditionMap) {
        if (player == null)
            return false;

        ItemStack enchantedItem = this.enchantment.getEnchantedItem(player, priorityItems);
        if (enchantedItem == null)
            return false;

        //Get the level of the enchantment
        int level = Util.getLevel(enchantedItem, this.enchantment.getEnchantment());

        //Check if this enchantment is still in cooldown for the player
        pendingCooldown.computeIfAbsent(player, v -> new HashMap<>());
        if (pendingCooldown.get(player).containsKey(this.enchantment.getEnchantment())) {
            if (enchantment.hasCoolDownMessage()) {
                Long startTime = pendingCooldown.get(player).get(this.enchantment.getEnchantment());
                int timeLeftSeconds = enchantment.getCooldown(level) - (int) ((System.currentTimeMillis() - startTime) / 1000);
                player.sendMessage(Util.replaceParameters(Map.of(
                        "player", player::getDisplayName,
                        "enchantment", enchantment::getName,
                        "time_left", () -> Util.secondsToString(timeLeftSeconds, false),
                        "time_left_full_out", () -> Util.secondsToString(timeLeftSeconds, true)
                ), enchantment.getCoolDownMessage()));
            }
            return false;
        }

        //Check if the trigger conditions are met
        if (!this.enchantment.checkTriggerConditions(type, triggerConditionMap))
            return false;

        //Return if chance didn't trigger
        if (RG.nextInt(10001) > enchantment.getChance(level))
            return false;

        //Cancel event if specified to do so
        if (e instanceof Cancellable event)
            event.setCancelled(enchantment.isCancelled(level));

        //Add cool down if necessary
        if (enchantment.getCooldown(level) > 0) {
            pendingCooldown.get(player).put(this.enchantment.getEnchantment(), System.currentTimeMillis());
            Bukkit.getScheduler()
                  .runTaskLater(
                          Main.getMain(),
                          v -> pendingCooldown.get(player).remove(this.enchantment.getEnchantment()),
                          enchantment.getCooldown(level) * 20L
                  );
        }

        //Set commands
        commands = enchantment.getCommands(level);

        return true;
    }

    private void dispatchCommand(int index, Map<String, Supplier<String>> parameters, Runnable onComplete) {
        if (index == commands.size()) {
            onComplete.run();
            return;
        }
        String command = commands.get(index);
        Matcher matcher = delayCommandPattern.matcher(command);
        if (matcher.matches())
            Bukkit.getScheduler()
                  .runTaskLater(
                          Main.getMain(),
                          () -> dispatchCommand(index + 1, parameters, onComplete),
                          Integer.parseInt(matcher.group(1)) * 20L
                  );
        else {
            //Replace parameters
            command = Util.replaceParameters(parameters, command);

            //Execute file functions
            try {
                command = FileFunction.parse(command);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            } catch (NumberFormatException exception) {
                Util.error("Error when trying to parse the functions the following command: " + command);
            }
            dispatchCommand(index + 1, parameters, onComplete);
        }
    }
}
