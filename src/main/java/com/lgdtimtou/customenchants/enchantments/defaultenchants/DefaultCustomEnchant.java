package com.lgdtimtou.customenchants.enchantments.defaultenchants;

import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchants.enchantments.defaultenchants.listeners.Replenish;
import com.lgdtimtou.customenchants.enchantments.defaultenchants.listeners.Telekinesis;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public enum DefaultCustomEnchant {

    //Enchantments
    REPLENISH("replenish", 1, Set.of(EnchantmentTarget.TOOL), new Replenish()),
    TELEKINESIS("telekinesis", 1, Set.of(EnchantmentTarget.BREAKABLE), new Telekinesis());


    private final String name;
    private final int maxLvl;
    private final Set<EnchantmentTarget> targets;
    private final CustomEnchantListener listener;

    DefaultCustomEnchant(String name, int maxLvl, Set<EnchantmentTarget> targets, CustomEnchantListener listener){
        this.name = name;
        this.maxLvl = maxLvl;
        this.targets = targets;
        this.listener = listener;
    }

    public Enchantment getEnchantment(){
        return Objects.requireNonNull(CustomEnchant.get(name)).getEnchantment();
    }

    public static void load(){
        Arrays.stream(values()).forEach(value -> new CustomEnchant(value.name, value.maxLvl, value.targets, value.listener));
    }

}
