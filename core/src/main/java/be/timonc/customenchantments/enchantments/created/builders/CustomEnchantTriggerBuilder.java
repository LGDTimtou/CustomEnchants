package be.timonc.customenchantments.enchantments.created.builders;

import be.timonc.customenchantments.enchantments.created.fields.CustomEnchantLevel;
import be.timonc.customenchantments.enchantments.created.fields.CustomEnchantTrigger;
import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerType;
import be.timonc.customenchantments.other.Util;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class CustomEnchantTriggerBuilder {

    private final String namespacedName;
    private final int maxLvl;
    private final ConfigurationSection config;
    private final Map<ConditionKey, Set<String>> conditions = new HashMap<>();
    private final List<CustomEnchantLevel> levels = new ArrayList<>();
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


    public CustomEnchantTrigger build() {
        if (!error) {
            return new CustomEnchantTrigger(
                    triggerInvoker,
                    conditions,
                    levels
            );
        }
        return null;
    }


    private void parseTriggerConditions() {
        ConfigurationSection triggerConditionSection = config.getConfigurationSection("conditions");
        if (triggerConditionSection == null) return;

        // Parse the trigger conditions types and its possible values
        for (String conditionKeyString : triggerConditionSection.getKeys(false)) {
            List<String> values = triggerConditionSection.getStringList(conditionKeyString);

            ConditionKey conditionKey;
            String[] parts = conditionKeyString.split("\\^");
            String triggerConditionTypeString = parts[0].trim().toUpperCase();
            String prefix = parts.length > 1 ? parts[1].trim().toLowerCase() : "";
            try {
                TriggerConditionType conditionType = TriggerConditionType.valueOf(triggerConditionTypeString);
                conditionKey = new ConditionKey(conditionType, prefix);
            } catch (IllegalArgumentException e) {
                String closest = Util.findClosestMatch(triggerConditionTypeString, TriggerConditionType.class);
                String closest_message = closest == null ? "." : ", did you mean " + closest + "?";
                Util.warn(namespacedName + ": " + triggerConditionTypeString + " is not a valid trigger condition type" + closest_message + " Skipping...");
                continue;
            }

            conditions.put(conditionKey, new HashSet<>(values));
        }
    }

    private void parseLevels() {
        ConfigurationSection levelSection = config.getConfigurationSection("levels");
        if (levelSection == null) {
            Util.error(namespacedName + ": TODO ZORGEN DAT LEVELS VAN VORIGE TRIGGER KRIJGT");
            error = true;
        }

        for (int i = 0; i < maxLvl; i++) {
            ConfigurationSection section = config.getConfigurationSection("levels." + (i + 1));
            CustomEnchantLevel level = new CustomEnchantLevelBuilder(
                    section,
                    i == 0 ? new CustomEnchantLevelBuilder().build() : this.levels.getLast()
            ).build();
            levels.add(level);
        }
    }
}
