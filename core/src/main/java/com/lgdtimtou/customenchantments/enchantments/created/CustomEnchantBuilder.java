package com.lgdtimtou.customenchantments.enchantments.created;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchantDefinition;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
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

    private boolean error;

    private final String namespacedName;
    private boolean enabled;


    private int enchantmentTableWeight;
    private int maxLvl;
    private int minCostBase;
    private int minCostIncr;
    private int maxCostBase;
    private int maxCostIncr;
    private int anvilCost;
    private boolean isCurse;
    private boolean isTreasure;
    private boolean isTradeable;
    private boolean inEnchantingTable;

    private Set<EnchantmentTarget> targets;
    private Set<String> conflictingEnchantments;


    private String coolDownMessage;
    private Map<EnchantTriggerType, Set<String>> triggers = Map.of();
    private final List<CustomEnchantLevel> levels = new ArrayList<>();


    public CustomEnchantBuilder(String name){
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
        if (triggerSection == null){
            Util.error(namespacedName + ": error when parsing 'triggers'");
            error = true;
            return;
        }

        this.triggers = Util.filterMap(triggerSection.getValues(false), EnchantTriggerType.class);
        if (this.triggers.isEmpty()){
            Util.error(namespacedName + ": no valid triggers");
            error = true;
            return;
        }

        EnchantTriggerType.fixOverrides(this.triggers);

        //Parsing optional cool down message
        this.coolDownMessage = config.getString(namespacedName + ".cooldown_message");


        //Parsing each level
        for (int i = 1; i <= maxLvl; i++){
            ConfigurationSection section = config.getConfigurationSection(namespacedName + ".levels." + i);
            if (section == null){
                Util.error(namespacedName + ": error when parsing level " + i);
                error = true;
                return;
            }
            this.levels.add(new CustomEnchantLevel(section, i == 1 ? new CustomEnchantLevel() : this.levels.get(this.levels.size() - 1)));
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

        //Parsing if the enchantment is in the enchantment table
        if (!config.isBoolean(path + "enchanting_table.included"))
            inEnchantingTable = true;
        else
            inEnchantingTable = config.getBoolean(path + "enchanting_table.included");

        //Parsing the enchantment table weight
        enchantmentTableWeight = config.getInt(path + "enchanting_table.weight");
        if (enchantmentTableWeight < 0 || enchantmentTableWeight > 1024) {
            Util.error(namespacedName + ": 'enchanting_table.weight' must be withing range [1:1024]");
            error = true;
            return false;
        }
        else if (enchantmentTableWeight == 0)
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

        //Parsing if the enchantment is a curse
        isCurse = config.isBoolean(path + "is_curse") && config.getBoolean(path + "is_curse");

        //Parsing if the enchantment is a treasure
        isTreasure = config.isBoolean(path + "is_treasure") && config.getBoolean(path + "is_treasure");

        //Parsing if the enchantment is tradeable
        isTradeable = config.isBoolean(path + "is_tradeable") && config.getBoolean(path + "is_tradeable");


        //Parsing the enchantment target items
        String targets = config.getString(path + "targets");
        if (targets == null)
            this.targets = Arrays.stream(EnchantmentTarget.values()).collect(Collectors.toSet());
        else
            this.targets = (HashSet<EnchantmentTarget>) Util.filterEnumNames(Util.yamlListToStream(targets), EnchantmentTarget.class, Collectors.toCollection(HashSet::new));

        if (this.targets.isEmpty())
            this.targets = Arrays.stream(EnchantmentTarget.values()).collect(Collectors.toSet());

        //Parse the conflicting enchantments
        this.conflictingEnchantments = Util.yamlListToStream(config.getString(path + "conflicts_with")).collect(Collectors.toSet());

        return true;
    }

    public void build(){
        if (error)
            Util.error(namespacedName + ": error during building process, fix above errors before building again");
        else if (enabled) {
            CustomEnchantDefinition definition = new CustomEnchantDefinition(
                    enchantmentTableWeight,
                    maxLvl,
                    new CustomEnchantDefinition.CustomEnchantCost(minCostBase, minCostIncr),
                    new CustomEnchantDefinition.CustomEnchantCost(maxCostBase, maxCostIncr),
                    anvilCost,
                    isCurse,
                    isTreasure,
                    isTradeable,
                    inEnchantingTable,
                    targets,
                    conflictingEnchantments
            );
            new CustomEnchant(namespacedName, definition, coolDownMessage, levels, triggers);
        }
    }

    public static class CustomEnchantLevel {

        private final int cooldown;
        private final double chance;
        private final boolean cancelEvent;
        private final List<String> commands;

        public CustomEnchantLevel(ConfigurationSection section, CustomEnchantLevel previous){
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

        CustomEnchantLevel(){
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
