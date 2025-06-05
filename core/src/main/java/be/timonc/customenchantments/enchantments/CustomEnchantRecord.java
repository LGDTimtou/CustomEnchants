package be.timonc.customenchantments.enchantments;

import be.timonc.customenchantments.other.Util;

import java.util.Map;
import java.util.Set;

public class CustomEnchantRecord {
    protected final String name;
    protected final String namespacedName;
    protected final CustomEnchantDefinition customEnchantDefinition;

    protected CustomEnchantRecord(String name, CustomEnchantDefinition customEnchantDefinition) {
        this.name = name;
        this.namespacedName = Util.getNamedspacedName(name);
        this.customEnchantDefinition = customEnchantDefinition;
    }

    public String getName() {
        return name;
    }

    public String getNamespacedName() {
        return namespacedName;
    }

    public boolean needPermission() {
        return customEnchantDefinition.needPermission();
    }

    public Set<String> getSupportedItems() {
        return customEnchantDefinition.supportedItems();
    }

    public Set<String> getPrimaryItems() {
        return customEnchantDefinition.primaryItems();
    }

    public Map<String, Boolean> getTags() {
        return customEnchantDefinition.tags();
    }

    public int getEnchantmentTableWeight() {
        return customEnchantDefinition.enchantmentTableWeight();
    }

    public int getMaxLevel() {
        return customEnchantDefinition.maxLevel();
    }

    public CustomEnchantDefinition.CustomEnchantCost getMinCost() {
        return customEnchantDefinition.minCost();
    }

    public CustomEnchantDefinition.CustomEnchantCost getMaxCost() {
        return customEnchantDefinition.maxCost();
    }

    public int getAnvilCost() {
        return customEnchantDefinition.anvilCost();
    }


    public Set<String> getConflictingEnchantments() {
        return customEnchantDefinition.conflictingEnchantments();
    }

    public Double getDestroyItemChance() {
        return customEnchantDefinition.destroyItemChance();
    }

    public Double getRemoveEnchantmentChance() {
        return customEnchantDefinition.removeEnchantmentChance();
    }
}
