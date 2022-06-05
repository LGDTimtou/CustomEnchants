package com.lgdtimtou.customenchants.enchantments.defaultenchants;

import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchants.enchantments.defaultenchants.listeners.HeadHunter;
import com.lgdtimtou.customenchants.enchantments.defaultenchants.listeners.listeners.Replenish;
import com.lgdtimtou.customenchants.enchantments.defaultenchants.listeners.listeners.Telekinesis;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.Objects;

public enum DefaultCustomEnchant {

    //Enchantments
    REPLENISH("replenish", 1, new Replenish()),
    TELEKINESIS("telekinesis", 1, new Telekinesis());
    //HEAD_HUNTER("head_hunter", 3, new HeadHunter());


    private final String name;
    private final int maxLvl;
    private final CustomEnchantListener listener;

    DefaultCustomEnchant(String name, int maxLvl, CustomEnchantListener listener){
        this.name = name;
        this.maxLvl = maxLvl;
        this.listener = listener;
    }

    public Enchantment getEnchantment(){
        return Objects.requireNonNull(CustomEnchant.get(name)).getEnchantment();
    }

    public static void load(){
        Arrays.stream(values()).forEach(value -> new CustomEnchant(value.name, value.maxLvl, value.listener));
    }

}
