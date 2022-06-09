package com.lgdtimtou.customenchants.enchantments.created;

import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.other.Files;
import com.lgdtimtou.customenchants.other.Util;
import org.bukkit.ChatColor;
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
    private ArrayList<EnchantTriggerType> triggerTypes;
    private Set<EnchantmentTarget> targets;
    private final List<CustomEnchantLevelInfo> levels = new ArrayList<>();


    public CustomEnchantBuilder(String n){
        this.error = false;
        this.name = n.toLowerCase();
        FileConfiguration config = Files.ENCHANTMENTS.getConfig();

        enabled = config.getBoolean(name + ".enabled");
        if (!enabled) return;

        maxLvl = config.getInt(name + ".max_level");
        if (maxLvl == 0) {
            error = true;
            return;
        }

        String triggers = config.getString(name + ".triggers");
        if (triggers == null) {
            error = true;
            return;
        }

        triggerTypes = (ArrayList<EnchantTriggerType>) Util.filter(triggers, EnchantTriggerType.values(), EnchantTriggerType.class, Collectors.toCollection(ArrayList::new));
        EnchantTriggerType.fixOverrides(triggerTypes);

        if (triggerTypes.isEmpty()){
            error = true;
            return;
        }


        String targets = config.getString(name + ".targets");
        if (targets == null)
            this.targets = Arrays.stream(EnchantmentTarget.values()).collect(Collectors.toSet());
        else
            this.targets = (HashSet<EnchantmentTarget>) Util.filter(targets, EnchantmentTarget.values(), EnchantmentTarget.class, Collectors.toCollection(HashSet::new));

        for (int i = 1; i <= maxLvl; i++)
            levels.add(new CustomEnchantLevelInfo(name, i));


        if (levels.stream().anyMatch(level -> !level.isEnabled())){
            error = true;
            Util.log(ChatColor.RED + "There was an error getting info from the " + name + " enchantment levels");
            Util.log(ChatColor.RED + "Make sure that all levels (1 -> max_level) are included and have a valid chance and commands list");
        }

        levels.forEach(level -> {
            if (level.getInheritCommandsFrom() > 0 && level.getInheritCommandsFrom() <= maxLvl)
                level.getCommands().addAll(levels.get(level.getInheritCommandsFrom() - 1).getCommands());
        });

        levels.forEach(level -> {
            if (level.getCommands().isEmpty())
                Util.log(ChatColor.RED + name + " enchant level: " + level.getLevel() + ", does not have any commands");
        });
    }

    public void build(){
        if (error)
            Util.log(ChatColor.RED + "Cannot Build " + name + ", check enchantments.yml for any syntax errors");
        else if (enabled)
            new CustomEnchant(name, maxLvl, triggerTypes, targets, levels);
    }



    //Info about each level of the enchantment (chance and commands)
    public static class CustomEnchantLevelInfo {

        private final int level;
        private final boolean enabled;

        private final int cooldown;
        private double chance;
        private final boolean cancelEvent;
        private final List<String> commands;

        private int inheritCommandsFrom;

        public CustomEnchantLevelInfo(String name, int level){
            this.level = level;
            FileConfiguration config = Files.ENCHANTMENTS.getConfig();
            enabled = true;

            cooldown = config.getInt(name + ".levels." + level + ".cooldown");

            chance = config.getDouble(name + ".levels." + level + ".chance");
            if (chance > 100 || chance <= 0) chance = 100;
            cancelEvent = config.getBoolean(name + ".levels." + level + ".cancel_event");
            commands = config.getStringList(name + ".levels." + level + ".commands");

            inheritCommandsFrom = config.getInt(name + ".levels." + level + ".inherit_commands_from");
            if (commands.isEmpty() && inheritCommandsFrom == 0)
                inheritCommandsFrom = 1;
        }

        public int getLevel() {
            return level;
        }

        private boolean isEnabled(){
            return enabled;
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

        private int getInheritCommandsFrom() {
            return inheritCommandsFrom;
        }

        public List<String> getCommands() {
            return commands;
        }
    }
}
