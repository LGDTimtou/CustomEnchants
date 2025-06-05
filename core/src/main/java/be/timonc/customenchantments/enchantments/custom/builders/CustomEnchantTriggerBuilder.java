package be.timonc.customenchantments.enchantments.custom.builders;

import be.timonc.customenchantments.enchantments.custom.fields.Level;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.Trigger;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerType;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.CustomTriggerCondition;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerCondition;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionValue;
import be.timonc.customenchantments.other.Util;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class CustomEnchantTriggerBuilder {

    private final String namespacedName;
    private final int maxLvl;
    private final ConfigurationSection config;
    private final Map<TriggerCondition, Set<TriggerConditionValue>> conditions = new HashMap<>();
    private final Set<CustomTriggerCondition> customConditions = new HashSet<>();
    private final List<Level> levels = new ArrayList<>();
    private TriggerType triggerType;
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
            triggerType = TriggerType.valueOf(triggerKey.toUpperCase());
            triggerType.getOrCreateInstance();
        } catch (IllegalArgumentException e) {
            String closest = Util.findClosestMatch(triggerKey, TriggerType.class);
            String closest_message = closest == null ? "." : ", did you mean " + closest + "?";
            Util.warn(namespacedName + ": " + triggerKey.toUpperCase() + " is not a valid trigger" + closest_message + " Skipping...");
            error = true;
            return;
        }

        //Parse the trigger condition parameters
        parseTriggerConditions();
        parseCustomTriggerConditions();

        //Parse the trigger specific levels
        parseLevels();
    }


    public Trigger build() {
        if (!error) {
            return new Trigger(
                    triggerType,
                    conditions,
                    customConditions,
                    levels
            );
        }
        return null;
    }

    private void parseCustomTriggerConditions() {
        List<String> conditionList = config.getStringList("custom_conditions");
        conditionList.forEach(customCondition -> customConditions.add(new CustomTriggerCondition(customCondition)));
    }


    private void parseTriggerConditions() {
        List<Map<?, ?>> conditionList = config.getMapList("conditions");

        for (Map<?, ?> _entry : conditionList) {
            Map<String, Object> entry = (Map<String, Object>) _entry;
            String group = (String) entry.get("group");
            if (group == null) {
                Util.error(namespacedName + ": missing 'group' in trigger condition");
                continue;
            }

            String prefix = (String) entry.getOrDefault("prefix", "");
            String suffix = (String) entry.getOrDefault("suffix", "");

            TriggerConditionGroupType triggerConditionGroupType;
            try {
                triggerConditionGroupType = TriggerConditionGroupType.valueOf(group.toUpperCase());
            } catch (IllegalArgumentException e) {
                String closest = Util.findClosestMatch(group, TriggerConditionGroupType.class);
                String closest_message = closest == null ? "." : ", did you mean " + closest + "?";
                Util.warn(namespacedName + ": " + group.toUpperCase() + " is not a valid trigger condition group" + closest_message + " Skipping...");
                continue;
            }

            String name = Util.getCombinedString("_", prefix, triggerConditionGroupType.name().toLowerCase(), suffix);
            TriggerCondition triggerCondition = triggerType.getTriggerCondition(
                    triggerConditionGroupType,
                    name
            );
            if (triggerCondition == null) {
                Util.error(namespacedName + ": " + name + " is not a valid trigger condition");
                continue;
            }

            List<Map<?, ?>> unparsedValues = (List<Map<?, ?>>) entry.get("values");
            Set<TriggerConditionValue> triggerConditionValues = parseTriggerConditionValues(unparsedValues);
            conditions.put(triggerCondition, triggerConditionValues);
        }
    }

    private Set<TriggerConditionValue> parseTriggerConditionValues(List<Map<?, ?>> unparsedTriggerConditionValues) {
        Set<TriggerConditionValue> triggerConditionValues = new HashSet<>();

        for (Map<?, ?> valueMap : unparsedTriggerConditionValues) {
            if (valueMap.get("operator") == null || valueMap.get("value") == null) {
                Util.error(namespacedName + ": Every trigger condition value should have an 'operator' and 'value' field");
                continue;
            }

            String operatorString = valueMap.get("operator").toString();
            String value = valueMap.get("value").toString();

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

        return triggerConditionValues;
    }

    private void parseLevels() {
        ConfigurationSection levelSection = config.getConfigurationSection("levels");
        if (levelSection == null) {
            Util.error(namespacedName + ": No levels specified for trigger: " + triggerType);
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
