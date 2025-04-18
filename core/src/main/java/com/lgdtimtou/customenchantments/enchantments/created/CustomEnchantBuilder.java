package com.lgdtimtou.customenchantments.enchantments.created;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchantDefinition;
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
    private boolean error;
    private boolean enabled;
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
        if (!parseDefinition(config, namespacedName, false))
            return;

        //Parse the triggers and its condition parameters
        ConfigurationSection triggerSection = config.getConfigurationSection(namespacedName + ".triggers");
        if (triggerSection == null) {
            Util.error(namespacedName + ": no 'triggers' section was found");
            error = true;
            return;
        }

        if (!parseTriggerMap(triggerSection)) {
            error = true;
            return;
        }

        removeOverriddenTriggers();

        //Parsing optional cool down message
        this.coolDownMessage = config.getString(namespacedName + ".cooldown_message");

        //Parsing each level
        for (int i = 1; i <= maxLvl; i++) {
            ConfigurationSection section = config.getConfigurationSection(namespacedName + ".levels." + i);
            if (section == null) {
                Util.error(namespacedName + ": error when parsing level " + i);
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


    private boolean parseDefinition(FileConfiguration config, String enchantId, boolean defaultEnchantment) {
        String path = enchantId + ".definition.";

        //Parsing the max level of the enchantment
        if (!defaultEnchantment) {
            maxLvl = config.getInt(path + "max_level");
            if (maxLvl <= 0) {
                Util.error(namespacedName + ": 'max_level' must be greater than 0");
                error = true;
                return false;
            }
        }

        //Parsing the enchantment table weight
        enchantmentTableWeight = config.getInt(path + "enchanting_table.weight");
        if (enchantmentTableWeight < 0 || enchantmentTableWeight > 1024) {
            Util.error(namespacedName + ": 'enchanting_table.weight' must be withing range [1:1024]");
            error = true;
            return false;
        } else if (enchantmentTableWeight == 0)
            enchantmentTableWeight = 5;

        //Parsing the min cost
        minCostBase = config.getInt(path + "enchanting_table.min_cost_base", -1);
        if (minCostBase == -1)
            minCostBase = 1;

        minCostIncr = config.getInt(path + "enchanting_table.min_cost_incr", -1);
        if (minCostIncr == -1)
            minCostIncr = 5;

        //Parsing the max cost
        maxCostBase = config.getInt(path + "enchanting_table.max_cost_base", -1);
        if (maxCostBase == -1)
            maxCostBase = 10;

        maxCostIncr = config.getInt(path + "enchanting_table.max_cost_incr", -1);
        if (maxCostIncr == -1)
            maxCostIncr = 5;

        //Parsing the anvil cost
        anvilCost = config.getInt(path + "anvil_cost", -1);
        if (anvilCost == -1)
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

        return true;
    }

    private boolean parseTriggerMap(ConfigurationSection triggerSection) {
        for (String triggerKey : triggerSection.getKeys(false)) {
            // Parse the trigger type
            EnchantTriggerType triggerType;
            try {
                triggerType = EnchantTriggerType.valueOf(triggerKey.toUpperCase());
            } catch (IllegalArgumentException e) {
                Util.error(namespacedName + ": " + triggerKey.toUpperCase() + " is not a valid trigger");
                return false;
            }

            List<Map<?, ?>> conditionList = triggerSection.getMapList(triggerKey);
            Map<ConditionKey, Set<String>> conditions = new HashMap<>();

            // Parse the trigger conditions types and its possible values
            for (Map<?, ?> conditionMap : conditionList) {
                for (Map.Entry<?, ?> entry : conditionMap.entrySet()) {
                    String conditionKeyString = entry.getKey().toString().toLowerCase();
                    Object value = entry.getValue();

                    ConditionKey conditionKey;
                    try {
                        String[] parts = conditionKeyString.split("\\^");
                        TriggerConditionType conditionType = TriggerConditionType.valueOf(parts[0].trim()
                                                                                                  .toUpperCase());
                        String prefix = parts.length > 1 ? parts[1].trim().toLowerCase() : "";
                        conditionKey = new ConditionKey(conditionType, prefix);
                    } catch (IllegalArgumentException e) {
                        Util.error(namespacedName + ": " + conditionKeyString + " is not a valid trigger condition type");
                        return false;
                    }

                    Set<String> valueSet = new HashSet<>();
                    if (value instanceof List<?>)
                        for (Object item : (List<?>) value)
                            valueSet.add(item.toString());
                    else valueSet.add(value.toString());

                    conditions.put(conditionKey, valueSet);
                }
            }

            this.triggers.put(triggerType, conditions);
        }
        return true;
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

    public void build() {
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
                    conflictingEnchantments
            );
            new CustomEnchant(namespacedName, definition, tags, coolDownMessage, levels, triggers);
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
