package com.lgdtimtou.customenchants.enchantments;

import com.lgdtimtou.customenchants.other.Files;
import com.lgdtimtou.customenchants.other.Util;
import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.defaultenchants.DefaultCustomEnchant;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class CustomEnchant {

    private static final String[] roman = new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    private static final Map<String, CustomEnchant> enchantments = new HashMap<>();

    private final String name;
    private final Enchantment enchantment;

    public CustomEnchant(String name, int maxLvl, CustomEnchantListener listener){
        this.name = name;
        this.enchantment = new EnchantmentWrapper(name, Util.title(name), maxLvl);
        enchantments.put(name, this);
        Util.registerEvent(listener);
    }

    public CustomEnchant(String name, int maxLvl, EnchantTriggerType type, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels){
        this.name = name;
        this.enchantment = new EnchantmentWrapper(name, Util.title(name), maxLvl);
        enchantments.put(name, this);

        CustomEnchantListener listener = type.getTrigger(enchantment, levels);
        Util.registerEvent(listener);
    }


    public Enchantment getEnchantment() {
        return enchantment;
    }

    public String getName() {
        return name;
    }

    public String getLoreName() {
        return Util.title(name.replaceAll("_", " "));
    }

    public static CustomEnchant get(String name){
        if (!enchantments.containsKey(name))
            throw new IllegalArgumentException(name + " enchantment does not exist");
        return enchantments.get(name);
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

        Util.log(ChatColor.GREEN + "Successfully registered these enchantments: " + getCustomEnchantSet().stream().map(CustomEnchant::getName).toList());
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
