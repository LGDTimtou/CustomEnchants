package be.timonc.customenchantments.enchantments.created.builders;

import be.timonc.customenchantments.enchantments.CustomEnchantDefinition;
import be.timonc.customenchantments.other.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.*;
import java.util.stream.Collectors;

public class CustomEnchantDefinitionBuilder {

    private final Map<String, Boolean> tags = new HashMap<>();
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
    private Set<EnchantmentTarget> targets;

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

        //Parsing the enchantment target items
        String targets = config.getString("targets");
        if (targets == null)
            this.targets = Arrays.stream(EnchantmentTarget.values()).collect(Collectors.toSet());
        else
            this.targets = (HashSet<EnchantmentTarget>) Util.filterEnumNames(
                    Util.yamlListToStream(targets),
                    EnchantmentTarget.class,
                    Collectors.toCollection(HashSet::new)
            );

        if (this.targets.isEmpty())
            this.targets = Arrays.stream(EnchantmentTarget.values()).collect(Collectors.toSet());

        //Parse the conflicting enchantments
        this.conflictingEnchantments = Util.yamlListToStream(config.getString("conflicts_with"))
                                           .collect(Collectors.toSet());

        //Parsing whether the enchantment should destroy the enchanted item
        destroyItemChance = config.getDouble("destroy_item_chance", 0);

        //Parsing whether the enchantment should remove the enchantment from the enchanted item
        removeEnchantmentChance = config.getDouble("remove_enchantment_chance", 0);
    }


    public CustomEnchantDefinition build() {
        if (!error)
            return new CustomEnchantDefinition(
                    enchantmentTableWeight,
                    maxLvl,
                    needsPermission,
                    new CustomEnchantDefinition.CustomEnchantCost(minCostBase, minCostIncr),
                    new CustomEnchantDefinition.CustomEnchantCost(maxCostBase, maxCostIncr),
                    anvilCost,
                    targets,
                    conflictingEnchantments,
                    tags,
                    destroyItemChance,
                    removeEnchantmentChance
            );
        return null;
    }
}
