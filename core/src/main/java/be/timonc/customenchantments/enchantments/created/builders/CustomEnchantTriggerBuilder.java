package be.timonc.customenchantments.enchantments.created.builders;

import be.timonc.customenchantments.enchantments.created.fields.Level;
import be.timonc.customenchantments.enchantments.created.fields.Trigger;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerCondition;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionValue;
import be.timonc.customenchantments.other.Util;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class CustomEnchantTriggerBuilder {

    private final String namespacedName;
    private final int maxLvl;
    private final ConfigurationSection config;
    private final Map<TriggerCondition, Set<TriggerConditionValue>> conditions = new HashMap<>();
    private final List<Level> levels = new ArrayList<>();
    private TriggerInvoker triggerInvoker;
    private boolean error = false;


    public CustomEnchantTriggerBuilder(String namespacedName, int maxLvl, String triggerKey, ConfigurationSection config) {
        this.namespacedName = namespacedName;
        this.maxLvl = maxLvl;
        this.config = config;
        if (config == null) {
            error = true;
            return;
        }

        try {
            TriggerType triggerType = TriggerType.valueOf(triggerKey.toUpperCase());
            triggerInvoker = new TriggerInvoker(triggerType);
        } catch (IllegalArgumentException e) {
            String closest = Util.findClosestMatch(triggerKey, TriggerType.class);
            String closest_message = closest == null ? "." : ", did you mean " + closest + "?";
            Util.warn(namespacedName + ": " + triggerKey.toUpperCase() + " is not a valid trigger" + closest_message + " Skipping...");
            error = true;
            return;
        }

        //Parse the trigger condition parameters
        parseTriggerConditions();

        //Parse the trigger specific levels
        parseLevels();
    }


    public Trigger build() {
        if (!error) {
            return new Trigger(
                    triggerInvoker,
                    conditions,
                    levels
            );
        }
        return null;
    }


    private void parseTriggerConditions() {
        List<Map<?, ?>> conditionList = config.getMapList("conditions");

        for (Map<?, ?> conditionEntry : conditionList) {
            for (Map.Entry<?, ?> conditionTypeEntry : conditionEntry.entrySet()) {
                String conditionType = (String) conditionTypeEntry.getKey();
                TriggerConditionType triggerConditionType;
                try {
                    triggerConditionType = TriggerConditionType.valueOf(conditionType.toUpperCase());
                } catch (IllegalArgumentException e) {
                    String closest = Util.findClosestMatch(conditionType, TriggerConditionType.class);
                    String closest_message = closest == null ? "." : ", did you mean " + closest + "?";
                    Util.warn(namespacedName + ": " + conditionType.toUpperCase() + " is not a valid trigger condition type" + closest_message + " Skipping...");
                    return;
                }

                Map<?, ?> conditionData = (Map<?, ?>) conditionTypeEntry.getValue();
                String prefix = (String) conditionData.get("prefix");
                List<Map<?, ?>> unparsedValues = (List<Map<?, ?>>) conditionData.get("values");

                TriggerCondition triggerCondition = triggerInvoker.getCondition(triggerConditionType, prefix);
                Set<TriggerConditionValue> triggerConditionValues = parseTriggerConditionValues(unparsedValues);
                conditions.put(triggerCondition, triggerConditionValues);
            }
        }
    }

    private Set<TriggerConditionValue> parseTriggerConditionValues(List<Map<?, ?>> unparsedTriggerConditionValues) {
        Set<TriggerConditionValue> triggerConditionValues = new HashSet<>();

        for (Map<?, ?> valueMap : unparsedTriggerConditionValues) {
            for (Map.Entry<?, ?> valueEntry : valueMap.entrySet()) {
                String operatorString = (String) valueEntry.getKey();
                String value = (String) valueEntry.getValue();

                TriggerConditionValue.Operator operator;
                try {
                    operator = TriggerConditionValue.Operator.valueOf(operatorString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    String closest = Util.findClosestMatch(operatorString, TriggerConditionValue.Operator.class);
                    String closest_message = closest == null ? "." : ", did you mean " + closest + "?";
                    Util.warn(namespacedName + ": " + operatorString.toUpperCase() + " is not a valid operator" + closest_message + " Skipping...");
                    continue;
                }

                triggerConditionValues.add(new TriggerConditionValue(operator, value));
            }
        }

        return triggerConditionValues;
    }

    private void parseLevels() {
        ConfigurationSection levelSection = config.getConfigurationSection("levels");
        if (levelSection == null) {
            Util.error(namespacedName + ": No levels specified for trigger: " + triggerInvoker.getTriggerType());
            error = true;
        }

        for (int i = 0; i < maxLvl; i++) {
            ConfigurationSection section = config.getConfigurationSection("levels." + (i + 1));
            Level level = new CustomEnchantLevelBuilder(
                    section,
                    i == 0 ? new CustomEnchantLevelBuilder().build() : this.levels.getLast()
            ).build();
            levels.add(level);
        }
    }
}
