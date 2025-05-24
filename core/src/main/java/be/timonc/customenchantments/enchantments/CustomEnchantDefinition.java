package be.timonc.customenchantments.enchantments;

import org.bukkit.enchantments.EnchantmentTarget;

import java.util.Map;
import java.util.Set;

public record CustomEnchantDefinition(
        int enchantmentTableWeight,
        int maxLevel,
        boolean needPermission,
        CustomEnchantCost minCost,
        CustomEnchantCost maxCost,
        int anvilCost,
        Set<EnchantmentTarget> enchantmentTargets,
        Set<String> conflictingEnchantments,
        Map<String, Boolean> tags,
        double destroyItemChance,
        double removeEnchantmentChance
) {
    public record CustomEnchantCost(int base, int perLevelAboveFirst) {
    }
}
