package be.timonc.customenchantments.enchantments;

import be.timonc.customenchantments.other.Util;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.Map;
import java.util.Set;

public class CustomEnchantRecord {
    protected final String name;
    protected final String namespacedName;
    protected final CustomEnchantDefinition customEnchantDefinition;
    private final Map<String, Boolean> tags;

    protected CustomEnchantRecord(String name, CustomEnchantDefinition customEnchantDefinition, Map<String, Boolean> tags) {
        this.name = name;
        this.namespacedName = Util.getNamedspacedName(name);
        this.customEnchantDefinition = customEnchantDefinition;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public String getNamespacedName() {
        return namespacedName;
    }

    public Map<String, Boolean> getTags() {
        return tags;
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

    public boolean needPermission() {
        return customEnchantDefinition.needPermission();
    }

    public Set<EnchantmentTarget> getEnchantmentTargets() {
        return customEnchantDefinition.enchantmentTargets();
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
