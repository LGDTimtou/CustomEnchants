package com.lgdtimtou.customenchantments.enchantments;

import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.Set;

public class CustomEnchantRecord {
    protected final String name;
    protected final String namespacedName;
    protected final CustomEnchantDefinition customEnchantDefinition;
    protected final String cooldownMessage;

    protected CustomEnchantRecord(String name, CustomEnchantDefinition customEnchantDefinition, String cooldownMessage) {
        this.name = name;
        this.namespacedName = Util.getNamedspacedName(name);
        this.customEnchantDefinition = customEnchantDefinition;
        this.cooldownMessage = cooldownMessage != null ? ChatColor.translateAlternateColorCodes('&', cooldownMessage) : null;
    }

    public String getName() {
        return name;
    }

    public String getNamespacedName() {
        return namespacedName;
    }

    public CustomEnchantDefinition getCustomEnchantDefinition() {
        return customEnchantDefinition;
    }

    public String getCoolDownMessage() {
        return cooldownMessage;
    }

    public boolean hasCoolDownMessage() {
        return cooldownMessage != null;
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

    public boolean isCurse() {
        return customEnchantDefinition.isCurse();
    }

    public boolean isTreasure() {
        return customEnchantDefinition.isTreasure();
    }

    public boolean isTradeable() {
        return customEnchantDefinition.isTradeable();
    }

    public boolean isInEnchantingTable() {
        return customEnchantDefinition.isInEnchantingTable();
    }

    public Set<EnchantmentTarget> getEnchantmentTargets() {
        return customEnchantDefinition.enchantmentTargets();
    }

    public Set<String> getConflictingEnchantments() {
        return customEnchantDefinition.conflictingEnchantments();
    }
}
