package be.timonc.customenchantments.enchantments.created.fields;

import be.timonc.customenchantments.Main;
import be.timonc.customenchantments.api.CustomEnchantTriggerEvent;
import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerType;
import be.timonc.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class CustomEnchantTrigger {

    private static final Map<Player, Map<CooldownKey, Long>> pendingCooldown = new HashMap<>();
    private final TriggerInvoker triggerInvoker;
    private final Map<ConditionKey, Set<String>> triggerConditions;
    private final List<CustomEnchantLevel> levels;
    private CustomEnchant customEnchant;


    public CustomEnchantTrigger(TriggerInvoker triggerInvoker, Map<ConditionKey, Set<String>> triggerConditions, List<CustomEnchantLevel> levels) {
        this.triggerInvoker = triggerInvoker;
        this.triggerConditions = triggerConditions;
        this.levels = levels;
    }

    public TriggerInvoker getInvoker() {
        return triggerInvoker;
    }

    public void setCustomEnchant(CustomEnchant customEnchant) {
        this.customEnchant = customEnchant;
    }


    public void executeInstructions(Event event, Player player, Set<ItemStack> priorityItems, Map<ConditionKey, Object> triggerConditionValues, Map<String, Supplier<String>> parameters, Runnable onComplete) {
        if (this.customEnchant == null) {
            Util.error("Custom Enchant not set in trigger: " + this.triggerInvoker);
            return;
        }

        // Add all global conditions
        triggerConditionValues.putAll(TriggerConditionType.getGlobalConditionMap(player));

        Queue<CustomEnchantInstruction> instructions = loadInstructions(
                event,
                player,
                priorityItems,
                parameters,
                triggerConditionValues
        );

        if (instructions == null) {
            onComplete.run();
            return;
        }

        Bukkit.getServer()
              .getPluginManager()
              .callEvent(new CustomEnchantTriggerEvent(
                      event,
                      player,
                      triggerInvoker.getTriggerType(),
                      customEnchant.getEnchantment(),
                      parameters
              ));

        CustomEnchantInstruction.executeInstructionQueue(
                new ArrayDeque<>(instructions),
                player,
                customEnchant,
                parameters,
                onComplete
        );
    }


    private Queue<CustomEnchantInstruction> loadInstructions(@NotNull Event event, @NotNull Player player, @NotNull Set<ItemStack> priorityItems, @NotNull Map<String, Supplier<String>> parameters, @NotNull Map<ConditionKey, Object> triggerConditionValues) {
        // Check if the player has permission to trigger this enchantment
        if (!customEnchant.hasPermission(player))
            return null;

        // Find the enchanted item in its set locations
        ItemStack enchantedItem = customEnchant.getEnchantedItem(player, priorityItems);
        if (enchantedItem == null)
            return null;

        //Get the level of the enchantment
        int levelValue = Util.getLevel(enchantedItem, customEnchant.getEnchantment());
        if (levelValue < 1 || levelValue > levels.size()) {
            Util.error("Level of " + customEnchant.getName() + " cannot be " + levelValue);
            return null;
        }
        CustomEnchantLevel level = levels.get(levelValue - 1);

        Map<String, Supplier<String>> globalParameters = getGlobalParameters(player, levelValue);
        populateParameters(parameters, globalParameters, triggerConditionValues);

        //Check if this enchantment is still in cooldown for the player
        CooldownKey cooldownKey = new CooldownKey(customEnchant, triggerInvoker.getTriggerType());
        pendingCooldown.computeIfAbsent(player, v -> new HashMap<>());
        if (pendingCooldown.get(player).containsKey(cooldownKey)) {
            if (level.cooldownMessage() != null && !level.cooldownMessage().isBlank()) {
                Long startTime = pendingCooldown.get(player).get(cooldownKey);
                int timeLeftSeconds = (int) (level.cooldown() - (double) (System.currentTimeMillis() - startTime) / 1000);
                globalParameters.put("time_left", () -> Util.secondsToString(timeLeftSeconds, false));
                globalParameters.put("time_left_full_out", () -> Util.secondsToString(timeLeftSeconds, true));
                player.sendMessage(Util.replaceParameters(player, level.cooldownMessage(), globalParameters));
            }
            return null;
        }

        //Check if the trigger conditions are met
        if (!checkTriggerConditions(triggerConditionValues))
            return null;

        //Return if chance didn't trigger
        double randomNumberEnchantment = Math.random() * 100;
        Util.debug("Random Number: " + randomNumberEnchantment + ", Enchantment Chance: " + level.chance());
        if (level.chance() < randomNumberEnchantment)
            return null;

        //Cancel event if specified to do so
        if (event instanceof Cancellable cancellable)
            cancellable.setCancelled(level.cancelEvent());

        //Add cool down if necessary
        if (level.cooldown() > 0) {
            pendingCooldown.get(player)
                           .put(
                                   cooldownKey,
                                   System.currentTimeMillis()
                           );
            Bukkit.getScheduler()
                  .runTaskLater(
                          Main.getMain(),
                          v -> pendingCooldown.get(player).remove(cooldownKey),
                          Double.valueOf(level.cooldown() * 20).longValue()
                  );
        }

        //Remove enchantment if chance triggered
        double randomNumberRemoveEnchantment = Math.random() * 100;
        Util.debug("Random Number: " + randomNumberRemoveEnchantment + ", Remove Enchantment Chance: " + customEnchant.getRemoveEnchantmentChance());
        if (randomNumberRemoveEnchantment < customEnchant.getRemoveEnchantmentChance())
            enchantedItem.removeEnchantment(customEnchant.getEnchantment());

        //Destroy item if chance triggered
        double randomNumberDestroyItem = Math.random() * 100;
        Util.debug("Random Number: " + randomNumberDestroyItem + ", Destroy Item Chance: " + customEnchant.getDestroyItemChance());
        if (randomNumberDestroyItem < customEnchant.getDestroyItemChance())
            enchantedItem.setAmount(enchantedItem.getAmount() - 1);

        // Return instructions
        return level.instructions();
    }

    private void populateParameters(Map<String, Supplier<String>> parameters, Map<String, Supplier<String>> globalParameters, Map<ConditionKey, Object> triggerConditionValues) {
        // Add global parameters
        parameters.putAll(globalParameters);

        // Add condition type parameters
        triggerConditionValues.forEach((conditionKey, obj) -> parameters.putAll(conditionKey.type()
                                                                                            .getConditionParameters(
                                                                                                    conditionKey.prefix(),
                                                                                                    obj
                                                                                            )));
    }


    private boolean checkTriggerConditions(Map<ConditionKey, Object> triggerConditionValues) {
        triggerConditions.forEach((conditionKey, strings) -> {
            if (!triggerConditionValues.containsKey(conditionKey))
                Util.warn(customEnchant.getNamespacedName() + ": " + conditionKey.toString()
                                                                                 .toUpperCase() + " is not a valid condition for " + triggerInvoker);
        });

        for (Map.Entry<ConditionKey, Object> entry : triggerConditionValues.entrySet()) {
            ConditionKey conditionKey = entry.getKey();
            Object triggerObject = entry.getValue();
            Set<String> conditions = triggerConditions.get(conditionKey);
            if (conditions != null)
                for (String condition : conditions)
                    if (!conditionKey.type().checkCondition(triggerObject, condition)) {
                        Util.debug(conditionKey + ":" + triggerObject + ", did not match any of the following " + conditions);
                        return false;
                    }
        }
        return true;
    }

    private Map<String, Supplier<String>> getGlobalParameters(Player player, int levelValue) {
        return new HashMap<>(Map.of(
                "player", player::getDisplayName,
                "player_x", () -> String.valueOf(player.getLocation().getX()),
                "player_y", () -> String.valueOf(player.getLocation().getY()),
                "player_z", () -> String.valueOf(player.getLocation().getZ()),
                "player_health", () -> String.valueOf(player.getHealth()),
                "enchantment", () -> customEnchant.getNamespacedName(),
                "enchantment_lore", () -> customEnchant.getName(),
                "enchantment_level", () -> String.valueOf(levelValue),
                "enchantment_level_roman", () -> CustomEnchant.getLevelRoman(levelValue)
        ));
    }

    public record CooldownKey(CustomEnchant customEnchant, TriggerType triggerType) {

    }
}
