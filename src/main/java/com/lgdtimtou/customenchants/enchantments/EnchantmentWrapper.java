package com.lgdtimtou.customenchants.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class EnchantmentWrapper extends Enchantment {

    private final String name;
    private final int maxLvl;
    private final Set<EnchantmentTarget> targets;


    public EnchantmentWrapper(String name, int maxLvl, Set<EnchantmentTarget> targets) {
        super(NamespacedKey.minecraft(name.toLowerCase()));
        this.name = name;
        this.maxLvl = maxLvl;
        this.targets = targets;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return maxLvl;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        boolean goed = false;
        for (EnchantmentTarget target : targets)
            if (target.includes(item))
                goed = true;
        return goed;
    }
}
