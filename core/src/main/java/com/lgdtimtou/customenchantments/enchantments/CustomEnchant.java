package com.lgdtimtou.customenchantments.enchantments;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.enchantments.created.CustomEnchantBuilder;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.defaultenchants.DefaultCustomEnchant;
import com.lgdtimtou.customenchantments.other.Files;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import java.util.*;


public class CustomEnchant extends CustomEnchantRecord {

    private static final String[] roman = new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    private static final Map<String, CustomEnchant> enchantments = new HashMap<>();

    private final List<CustomEnchantBuilder.CustomEnchantLevel> levels;
    private final Map<EnchantTriggerType, Set<String>> triggers;
    private final Enchantment enchantment;


    public CustomEnchant(String name, CustomEnchantDefinition definition, Map<String, Boolean> tags, String coolDownMessage, List<CustomEnchantBuilder.CustomEnchantLevel> levels, Map<EnchantTriggerType, Set<String>> triggers) {
        super(Util.getPrettyName(name), definition, tags, coolDownMessage);

        this.levels = levels;
        this.triggers = triggers;

        if (Main.isFirstBoot())
            this.enchantment = Main.getEnchantmentsManager().registerEnchantment(this);
        else
            this.enchantment = Util.getEnchantmentByName(this.namespacedName);

        if (this.enchantment == null) {
            Bukkit.getLogger().warning("Failed to register: " + this.namespacedName);
            return;
        }

        triggers.keySet().forEach(type -> Util.registerListener(type.getTrigger(this)));
        enchantments.put(this.namespacedName, this);
    }



    public Enchantment getEnchantment() {
        return enchantment;
    }

    //Info for each level

    public int getCooldown(int level){
        if (level <= 0 || level > levels.size())
            return -1;
        return levels.get(level - 1).getCooldown();
    }

    public int getChance(int level){
        if (level <= 0 || level > levels.size())
            return -1;
        return (int) levels.get(level - 1).getChance() * 100;
    }

    public boolean isCancelled(int level){
        if (level <= 0 || level > levels.size())
            return false;
        return levels.get(level - 1).isEventCancelled();
    }

    public List<String> getCommands(int level){
        if (level <= 0 || level > levels.size())
            return Collections.emptyList();
        return levels.get(level - 1).getCommands();
    }

    public boolean checkTriggerConditions(String triggerParameter, EnchantTriggerType type){
        Set<String> triggerConditions = triggers.get(type);
        if (triggerConditions == null || triggerConditions.isEmpty()) return true;
        return triggerConditions.stream().anyMatch(condition -> type.compareConditions(condition, triggerParameter));
    }


    //Registering
    public static void register() {
        if (Main.isFirstBoot())
            registerFirstBoot();
        else
            registerReload();
    }

    private static void registerFirstBoot() {
        Main.getEnchantmentsManager().unFreezeRegistry();

        //Build CustomEnchantments from enchantments.yml
        for (String enchant : Files.ENCHANTMENTS.getConfig().getConfigurationSection("").getValues(false).keySet())
            new CustomEnchantBuilder(enchant).build();

        //Register the default custom enchantments
        for (DefaultCustomEnchant defaultCustomEnchant : DefaultCustomEnchant.values())
            new CustomEnchantBuilder(defaultCustomEnchant).build();

        //Register the conflicting enchantments
        for (CustomEnchant customEnchant : getCustomEnchantSet())
            Main.getEnchantmentsManager().addExclusives(customEnchant.getNamespacedName(), customEnchant.getConflictingEnchantments());

        Main.getEnchantmentsManager().freezeRegistry();
        Util.log("Registered enchantments: " + getCustomEnchantSet().stream().map(CustomEnchant::getNamespacedName).toList());
    }

    private static void registerReload() {
        //Build CustomEnchantments from enchantments.yml
        for (String enchant : Files.ENCHANTMENTS.getConfig().getConfigurationSection("").getValues(false).keySet())
            new CustomEnchantBuilder(enchant).build();

        //Register the default custom enchantments
        for (DefaultCustomEnchant defaultCustomEnchant : DefaultCustomEnchant.values())
            new CustomEnchantBuilder(defaultCustomEnchant).build();

        Util.log("Registered enchantments: " + getCustomEnchantSet().stream().map(CustomEnchant::getNamespacedName).toList());
    }


    public static CustomEnchant get(String name){
        if (!enchantments.containsKey(name))
            throw new IllegalArgumentException(name + " enchantment does not exist");
        return enchantments.get(name);
    }

    public static Set<CustomEnchant> getCustomEnchantSet(){
        return new HashSet<>(enchantments.values());
    }


    public static String getLevelRoman(int level) {
        if (level < 1 || level > roman.length)
            return "error";
        return roman[level - 1];
    }
}
