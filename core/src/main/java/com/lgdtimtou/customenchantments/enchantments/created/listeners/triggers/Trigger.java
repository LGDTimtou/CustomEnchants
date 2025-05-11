package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchantments.enchantments.created.values.CustomEnchantInstruction;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class Trigger implements CustomEnchantListener {

    private static final Random RG = new Random();

    private static final Map<Player, Map<Enchantment, Long>> pendingCooldown = new HashMap<>();

    private static final Pattern delayCommandPattern = Pattern.compile("^[ ]*delay ([0-9]+)[ ]*$");

    private final CustomEnchant enchantment;

    private final EnchantTriggerType type;

    private Queue<CustomEnchantInstruction> instructions;

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

    protected void executeCommands(Event e, Player player, Set<ItemStack> priorityItems, Map<ConditionKey, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters, Runnable onComplete) {

        // Add all global conditions
        Map<ConditionKey, Object> mutableTriggerConditionMap = new HashMap<>(triggerConditionMap);
        mutableTriggerConditionMap.putAll(TriggerConditionType.getGlobalConditionMap(player));

        if (executeCommandsPreparation(e, player, priorityItems, mutableTriggerConditionMap)) {
            // Add global parameters to parameter map
            HashMap<String, Supplier<String>> parameters = new HashMap<>(localParameters);
            parameters.put("player", player::getDisplayName);
            parameters.put("player_x", () -> String.valueOf(player.getLocation().getX()));
            parameters.put("player_y", () -> String.valueOf(player.getLocation().getY()));
            parameters.put("player_z", () -> String.valueOf(player.getLocation().getZ()));
            parameters.put("player_health", () -> String.valueOf(player.getHealth()));

            // Add condition type parameters to parameter map
            mutableTriggerConditionMap.forEach((conditionKey, obj) -> parameters.putAll(conditionKey.type()
                                                                                                    .getConditionParameters(
                                                                                                            conditionKey.prefix(),
                                                                                                            obj
                                                                                                    )));
            CustomEnchantInstruction.executeInstructionQueue(
                    new ArrayDeque<>(instructions),
                    player,
                    this.enchantment,
                    parameters,
                    onComplete
            );
        } else {
            onComplete.run();
        }
    }

    protected boolean executeCommandsPreparation(@NotNull Event e, @NotNull Player player, @NotNull Set<ItemStack> priorityItems, @NotNull Map<ConditionKey, Object> triggerConditionMap) {
        // Check if the player has permission to trigger this enchantment
        if (!enchantment.hasPermission(player))
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
        double randomNumberEnchantment = Math.random() * 100;
        Util.debug("Random Number: " + randomNumberEnchantment + ", Enchantment Chance: " + enchantment.getChance(level));
        if (enchantment.getChance(level) < randomNumberEnchantment)
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

        //Remove enchantment if chance triggered
        double randomNumberRemoveEnchantment = Math.random() * 100;
        Util.debug("Random Number: " + randomNumberRemoveEnchantment + ", Remove Enchantment Chance: " + enchantment.getRemoveEnchantmentChance());
        if (randomNumberRemoveEnchantment < enchantment.getRemoveEnchantmentChance())
            enchantedItem.removeEnchantment(enchantment.getEnchantment());

        //Destroy item if chance triggered
        double randomNumberDestroyItem = Math.random() * 100;
        Util.debug("Random Number: " + randomNumberDestroyItem + ", Destroy Item Chance: " + enchantment.getDestroyItemChance());
        if (randomNumberDestroyItem < enchantment.getDestroyItemChance())
            enchantedItem.setAmount(enchantedItem.getAmount() - 1);

        //Set commands
        instructions = enchantment.getInstructions(level);

        return true;
    }

    private void dispatchCommand(int index, Map<String, Supplier<String>> parameters, Runnable onComplete) {
        //Util.debug(parameters.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue().get()).collect(
        //        Collectors.toSet()).toString());
        //if (index == instructions.size()) {
        //    onComplete.run();
        //    return;
        //}
        //String command = instructions.get(index);
        //Matcher matcher = delayCommandPattern.matcher(command);
        //if (matcher.matches())
        //    Bukkit.getScheduler()
        //          .runTaskLater(
        //                  Main.getMain(),
        //                  () -> dispatchCommand(index + 1, parameters, onComplete),
        //                  Integer.parseInt(matcher.group(1)) * 20L
        //          );
        //else {
        //    //Replace parameters
        //    command = Util.replaceParameters(parameters, command);
//
        //    //Execute file functions
        //    try {
        //        command = FileFunction.parse(command);
        //        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        //    } catch (NumberFormatException exception) {
        //        Util.error("Error when trying to parse the functions of the following command: " + command);
        //    }
        //    dispatchCommand(index + 1, parameters, onComplete);
        //}
    }
}
