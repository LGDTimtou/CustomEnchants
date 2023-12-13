package com.lgdtimtou.customenchants.enchantments.created;

import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.other.Files;
import com.lgdtimtou.customenchants.other.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.*;
import java.util.stream.Collectors;


//Loads a custom enchant from the enchantments.yml file with the given name
public class CustomEnchantBuilder {

    private boolean error;

    private final String name;
    private final boolean enabled;
    private int maxLvl;
    private Map<EnchantTriggerType, Set<String>> triggers = Map.of();
    private Set<EnchantmentTarget> targets;
    private final List<CustomEnchantLevel> levels = new ArrayList<>();


    public CustomEnchantBuilder(String n){
        this.error = false;
        this.name = n.toLowerCase();
        FileConfiguration config = Files.ENCHANTMENTS.getConfig();

        //Parsing the enabled option
        enabled = config.getBoolean(name + ".enabled");
        if (!enabled) return;

        //Parsing the max level of the enchantment
        maxLvl = config.getInt(name + ".max_level");
        if (maxLvl == 0) {
            Util.error(name + ": 'max_level' must be greater than 0");
            error = true;
            return;
        }

        //Parse the triggers and its condition parameters
        ConfigurationSection triggerSection = config.getConfigurationSection(name + ".triggers");
        if (triggerSection == null){
            Util.error(name + ": error when parsing 'triggers'");
            error = true;
            return;
        }

        this.triggers = Util.filterMap(triggerSection.getValues(false), EnchantTriggerType.class);
        if (this.triggers.isEmpty()){
            Util.error(name + ": no valid triggers");
            error = true;
            return;
        }
        EnchantTriggerType.fixOverrides(this.triggers);


        //Parsing the enchantment target items
        String targets = config.getString(name + ".targets");
        if (targets == null)
            this.targets = Arrays.stream(EnchantmentTarget.values()).collect(Collectors.toSet());
        else
            this.targets = (HashSet<EnchantmentTarget>) Util.filterEnumNames(Util.yamlListToStream(targets), EnchantmentTarget.class, Collectors.toCollection(HashSet::new));

        //Parsing each level
        for (int i = 1; i <= maxLvl; i++){
            ConfigurationSection section = config.getConfigurationSection(name + ".levels." + i);
            if (section == null){
                Util.error(name + ": error when parsing level " + i);
                return;
            }
            this.levels.add(new CustomEnchantLevel(section, i == 1 ? new CustomEnchantLevel() : this.levels.get(this.levels.size() - 1)));
        }
    }

    public void build(){
        if (error)
            Util.error(name + ": error during building process, fix above errors before building again");
        else if (enabled)
            new CustomEnchant(name, maxLvl, triggers, targets, levels);
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
