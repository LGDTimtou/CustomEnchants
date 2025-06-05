package be.timonc.customenchantments.enchantments.custom.builders;

import be.timonc.customenchantments.enchantments.CustomEnchantDefinition;
import be.timonc.customenchantments.other.Util;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomEnchantDefinitionBuilder {

    private final Map<String, Boolean> tags = new HashMap<>();
    private Set<String> supportedItems;
    private Set<String> primaryItems;
    private int maxLvl;
    private boolean needsPermission;
    private Set<String> conflictingEnchantments;
    private double destroyItemChance;
    private double removeEnchantmentChance;
    private boolean error = false;
    private int enchantmentTableWeight;
    private int minCostBase;
    private int minCostIncr;
    private int maxCostBase;
    private int maxCostIncr;
    private int anvilCost;

    public CustomEnchantDefinitionBuilder(String namespacedName, int maxLvl, ConfigurationSection config) {
        if (config == null) {
            Util.error(namespacedName + ": no definition found!");
            error = true;
            return;
        }
        this.maxLvl = maxLvl;

        //Parsing whether the permission should be loaded for this enchantment
        needsPermission = config.getBoolean("needs_permission", false);

        //Parsing the enchantment table weight
        enchantmentTableWeight = config.getInt("enchanting_table.weight");
        if (enchantmentTableWeight <= 0 || enchantmentTableWeight > 1024)
            enchantmentTableWeight = 5;

        //Parsing the min cost
        minCostBase = config.getInt("enchanting_table.min_cost_base", -1);
        if (minCostBase < 0)
            minCostBase = 1;

        minCostIncr = config.getInt("enchanting_table.min_cost_incr", -1);
        if (minCostIncr < 0)
            minCostIncr = 5;

        //Parsing the max cost
        maxCostBase = config.getInt("enchanting_table.max_cost_base", -1);
        if (maxCostBase < 0)
            maxCostBase = 10;

        maxCostIncr = config.getInt("enchanting_table.max_cost_incr", -1);
        if (maxCostIncr < 0)
            maxCostIncr = 5;

        //Parsing the anvil cost
        anvilCost = config.getInt("anvil_cost", -1);
        if (anvilCost < 0)
            anvilCost = 2;

        //Parse the tags
        ConfigurationSection tagSection = config.getConfigurationSection("tags");
        if (tagSection != null)
            tagSection.getKeys(false)
                      .forEach(key -> tags.put("minecraft:" + key.toLowerCase(), tagSection.getBoolean(key, false)));

        //Loading the supported and primary items for the enchantment
        supportedItems = new HashSet<>(config.getStringList("supported"));
        primaryItems = new HashSet<>(config.getStringList("primary"));
        if (supportedItems.isEmpty())
            supportedItems = new HashSet<>(Set.of("enchantable/durability"));
        if (primaryItems.isEmpty())
            primaryItems = supportedItems;
        primaryItems.retainAll(supportedItems);

        //Parse the conflicting enchantments
        this.conflictingEnchantments = new HashSet<>(config.getStringList("conflicts_with"));

        //Parsing whether the enchantment should destroy the enchanted item
        destroyItemChance = config.getDouble("destroy_item_chance", 0);

        //Parsing whether the enchantment should remove the enchantment from the enchanted item
        removeEnchantmentChance = config.getDouble("remove_enchantment_chance", 0);
    }


    public CustomEnchantDefinition build() {
        if (!error)
            return new CustomEnchantDefinition(
                    supportedItems,
                    primaryItems,
                    enchantmentTableWeight,
                    maxLvl,
                    needsPermission,
                    new CustomEnchantDefinition.CustomEnchantCost(minCostBase, minCostIncr),
                    new CustomEnchantDefinition.CustomEnchantCost(maxCostBase, maxCostIncr),
                    anvilCost,
                    conflictingEnchantments,
                    tags,
                    destroyItemChance,
                    removeEnchantmentChance
            );
        return null;
    }
}
