package com.lgdtimtou.customenchants.enchantments;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.defaultenchants.DefaultCustomEnchant;
import com.lgdtimtou.customenchants.other.Files;
import com.lgdtimtou.customenchants.other.Util;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class CustomEnchant {

    private static final String[] roman = new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    private static final Map<String, CustomEnchant> enchantments = new HashMap<>();

    private final String name;
    private final Enchantment enchantment;
    private final Set<EnchantmentTarget> targets;

    private final Map<EnchantTriggerType, Set<String>> triggers;

    private final List<CustomEnchantBuilder.CustomEnchantLevel> levels;

    public CustomEnchant(String name, int maxLvl, Set<EnchantmentTarget> targets, CustomEnchantListener listener){
        this.name = name;
        this.targets = targets;
        this.enchantment = new EnchantmentWrapper(name, maxLvl, targets);
        this.levels = Collections.emptyList();
        enchantments.put(name, this);

        this.triggers = Map.of();
        Util.registerListener(listener);
    }

    public CustomEnchant(String name, int maxLvl, Map<EnchantTriggerType, Set<String>> triggers, Set<EnchantmentTarget> targets, List<CustomEnchantBuilder.CustomEnchantLevel> levels){
        this.name = name;
        this.targets = targets;
        this.enchantment = new EnchantmentWrapper(name, maxLvl, targets);
        this.levels = levels;

        enchantments.put(name, this);

        this.triggers = triggers;
        triggers.keySet().forEach(type -> Util.registerListener(type.getTrigger(enchantment)));
    }


    public Enchantment getEnchantment() {
        return enchantment;
    }

    public String getName() {
        return name;
    }

    public Set<EnchantmentTarget> getTargets(){
        return targets;
    }

    public String getLore() {
        return Util.title(name.replaceAll("_", " "));
    }

    public static CustomEnchant get(String name){
        if (!enchantments.containsKey(name))
            throw new IllegalArgumentException(name + " enchantment does not exist");
        return enchantments.get(name);
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
    public static void register(){
        //Build CustomEnchantments from enchantments.yml
        for (String enchant : Files.ENCHANTMENTS.getConfig().getConfigurationSection("").getValues(false).keySet())
            new CustomEnchantBuilder(enchant).build();
        //Load default CustomEnchantments
        DefaultCustomEnchant.load();


        //Register all enchantments
        List<Enchantment> enchantments = Arrays.stream(Enchantment.values()).toList();
        for (Enchantment enchantment : getCustomEnchantments())
            if (!enchantments.contains(enchantment))
                registerEnchantment(enchantment);

        Util.log("Registered enchantments: " + getCustomEnchantSet().stream().map(CustomEnchant::getName).toList());
    }

    public static Set<CustomEnchant> getCustomEnchantSet(){
        return new HashSet<>(enchantments.values());
    }


    private static Set<Enchantment> getCustomEnchantments() {
        return enchantments.values().stream().map(CustomEnchant::getEnchantment).collect(Collectors.toSet());
    }


    private static void registerEnchantment(Enchantment enchantment){
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static String getLevelRoman(int level){
        if (level < 1 || level > roman.length)
            return "error";
        return roman[level - 1];
    }




}
