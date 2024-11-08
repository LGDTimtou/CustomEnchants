package com.lgdtimtou.customenchantments.enchantments;

import org.bukkit.enchantments.EnchantmentTarget;

import java.util.Set;

public record CustomEnchantDefinition(
        int enchantmentTableWeight,
        int maxLevel,
        CustomEnchantCost minCost,
        CustomEnchantCost maxCost,
        int anvilCost,
        Set<EnchantmentTarget> enchantmentTargets,
        Set<String> conflictingEnchantments
) {
    public record CustomEnchantCost(int base, int perLevelAboveFirst){}
}
