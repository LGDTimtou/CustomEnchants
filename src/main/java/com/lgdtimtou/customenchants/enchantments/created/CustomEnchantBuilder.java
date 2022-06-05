package com.lgdtimtou.customenchants.enchantments.created;

import com.lgdtimtou.customenchants.Files;
import com.lgdtimtou.customenchants.Util;
import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//Loads a custom enchant from the enchantments.yml file with the given name
public class CustomEnchantBuilder {

    private boolean error;

    private final String name;
    private final boolean enabled;
    private int maxLvl;
    private EnchantTriggerType triggerType;
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

        String trigger = config.getString(name + ".trigger");
        if (trigger == null) {
            error = true;
            return;
        }

        if (Arrays.stream(EnchantTriggerType.values()).noneMatch(v -> v.name().equals(trigger.toUpperCase()))){
            error = true;
            return;
        }
        triggerType = EnchantTriggerType.valueOf(trigger.toUpperCase());

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
            Util.log(Util.getMessage(ChatColor.RED + "Cannot Build " + name + ", check enchantments.yml for any syntax errors"));
        else if (enabled)
            new CustomEnchant(name, maxLvl, triggerType, levels);
    }



    //Info about each level of the enchantment (chance and commands)
    public static class CustomEnchantLevelInfo {

        private final int level;
        private boolean enabled;

        private final int chance;
        private final boolean cancelEvent;
        private final int inheritCommandsFrom;
        private final List<String> commands;

        public CustomEnchantLevelInfo(String name, int level){
            this.level = level;
            FileConfiguration config = Files.ENCHANTMENTS.getConfig();
            enabled = true;
            chance = config.getInt(name + ".levels." + level + ".chance");
            if (chance > 100 || chance <= 0) enabled = false;
            cancelEvent = config.getBoolean(name + ".levels." + level + ".cancel_event");
            inheritCommandsFrom = config.getInt(name + ".levels." + level + ".inherit_commands_from");
            commands = config.getStringList(name + ".levels." + level + ".commands");
        }

        public int getLevel() {
            return level;
        }

        private boolean isEnabled(){
            return enabled;
        }

        public int getChance() {
            return chance;
        }

        public boolean isEventCancelled() {
            return cancelEvent;
        }

        public int getInheritCommandsFrom() {
            return inheritCommandsFrom;
        }

        public List<String> getCommands() {
            return commands;
        }
    }
}
