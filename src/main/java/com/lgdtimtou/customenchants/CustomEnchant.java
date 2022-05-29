package com.lgdtimtou.customenchants;

import com.lgdtimtou.customenchants.enchantments.CustomEnchantListener;
import com.lgdtimtou.customenchants.enchantments.HeadHunter;
import com.lgdtimtou.customenchants.enchantments.Replenish;
import com.lgdtimtou.customenchants.enchantments.Telekinesis;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum CustomEnchant {

    //Enchantments
    REPLENISH("replenish", 1, new Replenish()),
    TELEKINESIS("telekinesis", 1, new Telekinesis()),
    HEAD_HUNTER("head_hunter", 3, new HeadHunter());

    private static final String[] roman = new String[]{"I", "II", "III", "IV", "V", "VI"};


    private final String name;
    private final Enchantment enchantment;

    CustomEnchant(String name, int maxLvl, CustomEnchantListener listener){
        this.name = Util.title(name);
        this.enchantment = new EnchantmentWrapper(name, Util.title(name), maxLvl);
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





    //Registering
    public static void register(){
        List<Enchantment> enchantments = Arrays.stream(Enchantment.values()).collect(Collectors.toList());
        for (Enchantment enchantment : getCustomEnchantments())
            if (!enchantments.contains(enchantment))
                registerEnchantment(enchantment);
    }


    private static Set<Enchantment> getCustomEnchantments() {
        return Arrays.stream(CustomEnchant.values()).map(CustomEnchant::getEnchantment).collect(Collectors.toSet());
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
