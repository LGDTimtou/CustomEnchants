package com.lgdtimtou.customenchantments.enchantments.created;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchantDefinition;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchantedItemLocation;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.defaultenchants.DefaultCustomEnchant;
import com.lgdtimtou.customenchantments.other.Files;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.*;
import java.util.stream.Collectors;


//Loads a custom enchant from the enchantments.yml file with the given name
public class CustomEnchantBuilder {

    private final String namespacedName;
    private final Map<String, Boolean> tags = new HashMap<>();
    private final Map<EnchantTriggerType, Map<ConditionKey, Set<String>>> triggers = new HashMap<>();
    private final List<CustomEnchantLevel> levels = new ArrayList<>();
    private final List<CustomEnchantedItemLocation> customEnchantedItemLocations = new ArrayList<>();
    private boolean error;
    private boolean enabled;
    private double destroyItemChance;
    private double removeEnchantmentChance;
    private int enchantmentTableWeight;
    private int maxLvl;
    private int minCostBase;
    private int minCostIncr;
    private int maxCostBase;
    private int maxCostIncr;
    private int anvilCost;
    private Set<EnchantmentTarget> targets;
    private Set<String> conflictingEnchantments;
    private String coolDownMessage;


    public CustomEnchantBuilder(String name) {
        this.error = false;
        this.namespacedName = Util.getNamedspacedName(name);
        FileConfiguration config = Files.ENCHANTMENTS.getConfig();

        //Parsing the enabled option
        enabled = config.getBoolean(namespacedName + ".enabled");
        if (!enabled) return;

        //Parsing the definition
        parseDefinition(config, namespacedName, false);

        //Parsing the custom locations for the enchanted item
        List<String> locations = config.getStringList(namespacedName + ".custom_locations");
        for (String location : locations) {
            try {
                customEnchantedItemLocations.add(CustomEnchantedItemLocation.valueOf(location.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                String closest = Util.findClosestMatch(location, CustomEnchantedItemLocation.class);
                String closest_message = closest == null ? "." : ", did you mean " + closest + "?";
                Util.warn(namespacedName + ": " + location.toUpperCase() + " is not a valid custom enchanted item location" + closest_message + " Skipping...");
            }
        }

        //Parsing whether the enchantment should destroy the enchanted item
        destroyItemChance = config.getDouble(namespacedName + ".destroy_item_chance", 0);
        removeEnchantmentChance = config.getDouble(namespacedName + ".remove_enchantment_chance", 0);

        //Parse the triggers and its condition parameters
        ConfigurationSection triggerSection = config.getConfigurationSection(namespacedName + ".triggers");
        parseTriggerMap(triggerSection);
        if (triggers.isEmpty())
            Util.error(namespacedName + ": no triggers defined; this enchantment will never activate.");
        removeOverriddenTriggers();

        //Parsing optional cool down message
        this.coolDownMessage = config.getString(namespacedName + ".cooldown_message");

        //Parsing each level
        for (int i = 1; i <= maxLvl; i++) {
            ConfigurationSection section = config.getConfigurationSection(namespacedName + ".levels." + i);
            if (section == null) {
                Util.error(namespacedName + ": no definition found for level " + i);
                error = true;
                return;
            }
            this.levels.add(new CustomEnchantLevel(
                    section,
                    i == 1 ? new CustomEnchantLevel() : this.levels.get(this.levels.size() - 1)
            ));
        }
    }

    public CustomEnchantBuilder(DefaultCustomEnchant defaultCustomEnchant) {
        this.namespacedName = defaultCustomEnchant.getNamespacedName();
        FileConfiguration config = Files.DEFAULT_ENCHANTMENTS.getConfig();

        if (!config.contains(this.namespacedName)) {
            Util.error("Default custom enchantment " + this.namespacedName + " has not been found in the default_enchantments.yml file");
            error = true;
            return;
        }

        //Parsing the enabled option
        enabled = config.getBoolean(namespacedName + ".enabled");
        if (!enabled) return;

        //Setting max level
        maxLvl = defaultCustomEnchant.getMaxLevel();

        //Parsing the definition
        parseDefinition(config, namespacedName, true);

        //Register the listener
        Util.registerListener(defaultCustomEnchant.getListener());
    }


    private void parseDefinition(FileConfiguration config, String enchantId, boolean defaultEnchantment) {
        String path = enchantId + ".definition.";

        //Parsing the max level of the enchantment
        if (!defaultEnchantment) {
            maxLvl = config.getInt(path + "max_level");
            if (maxLvl <= 0) {
                Util.error(namespacedName + ": 'max_level' must be greater than 0; using default value 1");
                maxLvl = 1;
            }
        }

        //Parsing the enchantment table weight
        enchantmentTableWeight = config.getInt(path + "enchanting_table.weight");
        if (enchantmentTableWeight <= 0 || enchantmentTableWeight > 1024) {
            Util.warn(namespacedName + ": 'enchanting_table.weight' must be in the range [1–1024]; using default value 5");
            enchantmentTableWeight = 5;
        }

        //Parsing the min cost
        minCostBase = config.getInt(path + "enchanting_table.min_cost_base", -1);
        if (minCostBase < 0)
            minCostBase = 1;

        minCostIncr = config.getInt(path + "enchanting_table.min_cost_incr", -1);
        if (minCostIncr < 0)
            minCostIncr = 5;

        //Parsing the max cost
        maxCostBase = config.getInt(path + "enchanting_table.max_cost_base", -1);
        if (maxCostBase < 0)
            maxCostBase = 10;

        maxCostIncr = config.getInt(path + "enchanting_table.max_cost_incr", -1);
        if (maxCostIncr < 0)
            maxCostIncr = 5;

        //Parsing the anvil cost
        anvilCost = config.getInt(path + "anvil_cost", -1);
        if (anvilCost < 0)
            anvilCost = 2;

        //Parse the tags
        ConfigurationSection tagSection = config.getConfigurationSection(namespacedName + ".definition.tags");
        if (tagSection != null)
            tagSection.getKeys(false)
                      .forEach(key -> tags.put("minecraft:" + key.toLowerCase(), tagSection.getBoolean(key, false)));

        //Parsing the enchantment target items
        String targets = config.getString(path + "targets");
        if (targets == null)
            this.targets = Arrays.stream(EnchantmentTarget.values()).collect(Collectors.toSet());
        else
            this.targets = (HashSet<EnchantmentTarget>) Util.filterEnumNames(
                    Util.yamlListToStream(targets),
                    EnchantmentTarget.class,
                    Collectors.toCollection(HashSet::new)
            );

        if (this.targets.isEmpty())
            this.targets = Arrays.stream(EnchantmentTarget.values()).collect(Collectors.toSet());

        //Parse the conflicting enchantments
        this.conflictingEnchantments = Util.yamlListToStream(config.getString(path + "conflicts_with"))
                                           .collect(Collectors.toSet());
    }

    private void parseTriggerMap(ConfigurationSection triggerSection) {
        if (triggerSection == null) return;
        for (String triggerKey : triggerSection.getKeys(false)) {
            // Parse the trigger type
            EnchantTriggerType triggerType;
            try {
                triggerType = EnchantTriggerType.valueOf(triggerKey.toUpperCase());
            } catch (IllegalArgumentException e) {
                String closest = Util.findClosestMatch(triggerKey, EnchantTriggerType.class);
                String closest_message = closest == null ? "." : ", did you mean " + closest + "?";
                Util.warn(namespacedName + ": " + triggerKey.toUpperCase() + " is not a valid trigger" + closest_message + " Skipping...");
                continue;
            }

            ConfigurationSection triggerConditionSection = triggerSection.getConfigurationSection(triggerKey);
            if (triggerConditionSection == null) continue;

            Map<ConditionKey, Set<String>> conditions = new HashMap<>();
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

            this.triggers.put(triggerType, conditions);
        }

        Util.debug(this.triggers.toString());
    }

    private void removeOverriddenTriggers() {
        Iterator<Map.Entry<EnchantTriggerType, Map<ConditionKey, Set<String>>>> it = this.triggers.entrySet()
                                                                                                  .iterator();
        while (it.hasNext()) {
            EnchantTriggerType type = it.next().getKey();
            // Loop through all types that can override this one
            for (EnchantTriggerType overriddenByType : type.getOverriddenBy()) {
                if (triggers.containsKey(overriddenByType)) {
                    // Remove the overridden trigger from the map
                    it.remove();
                    Util.error(namespacedName + ": the '" + type + "' trigger was overridden by '" + overriddenByType + "', so it has been removed");
                }
            }
        }
    }

    public void build(boolean defaultEnchantment) {
        if (error)
            Util.error(namespacedName + ": error during building process, fix above errors before building again");
        else if (enabled) {
            CustomEnchantDefinition definition = new CustomEnchantDefinition(
                    enchantmentTableWeight,
                    maxLvl,
                    new CustomEnchantDefinition.CustomEnchantCost(minCostBase, minCostIncr),
                    new CustomEnchantDefinition.CustomEnchantCost(maxCostBase, maxCostIncr),
                    anvilCost,
                    targets,
                    conflictingEnchantments,
                    destroyItemChance,
                    removeEnchantmentChance
            );
            new CustomEnchant(
                    namespacedName,
                    defaultEnchantment,
                    definition,
                    customEnchantedItemLocations,
                    tags,
                    coolDownMessage,
                    levels,
                    triggers
            );
        }
    }

    public static class CustomEnchantLevel {

        private final int cooldown;
        private final double chance;
        private final boolean cancelEvent;
        private final List<String> commands;

        public CustomEnchantLevel(ConfigurationSection section, CustomEnchantLevel previous) {
            int cooldown = section.getInt("cooldown", previous.cooldown);
            double chance = section.getDouble("chance", previous.chance);
            if (chance > 100 || chance <= 0) chance = 100;
            boolean cancelEvent = section.getBoolean("cancel_event", previous.cancelEvent);
            List<String> commands = section.getStringList("commands");
            if (commands.isEmpty()) commands = previous.commands;

            this.cooldown = cooldown;
            this.chance = chance;
            this.cancelEvent = cancelEvent;
            this.commands = commands;
        }

        CustomEnchantLevel() {
            this.cooldown = 0;
            this.chance = 100;
            this.cancelEvent = false;
            this.commands = Collections.emptyList();
        }


        public int getCooldown() {
            return cooldown;
        }

        public double getChance() {
            return chance;
        }

        public boolean isEventCancelled() {
            return cancelEvent;
        }

        public List<String> getCommands() {
            return commands;
        }
    }
}
