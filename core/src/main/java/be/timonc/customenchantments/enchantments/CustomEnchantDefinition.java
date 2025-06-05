package be.timonc.customenchantments.enchantments;

import java.util.Map;
import java.util.Set;

public record CustomEnchantDefinition(
        Set<String> supportedItems,
        Set<String> primaryItems,
        int enchantmentTableWeight,
        int maxLevel,
        boolean needPermission,
        CustomEnchantCost minCost,
        CustomEnchantCost maxCost,
        int anvilCost,
        Set<String> conflictingEnchantments,
        Map<String, Boolean> tags,
        double destroyItemChance,
        double removeEnchantmentChance
) {
    public record CustomEnchantCost(int base, int perLevelAboveFirst) {
    }
}
