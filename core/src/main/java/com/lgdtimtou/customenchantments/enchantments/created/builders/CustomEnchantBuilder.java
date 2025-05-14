package com.lgdtimtou.customenchantments.enchantments.created.builders;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchantDefinition;
import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantedItemLocation;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.defaultenchants.DefaultCustomEnchant;
import com.lgdtimtou.customenchantments.other.Files;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;


//Loads a custom enchant from the enchantments.yml file with the given name
public class CustomEnchantBuilder {

    private final String namespacedName;
    private final Map<String, Boolean> tags = new HashMap<>();
    private final List<CustomEnchantTrigger> triggers = new ArrayList<>();
    private final List<CustomEnchantedItemLocation> customEnchantedItemLocations = new ArrayList<>();
    private int maxLvl;
    private boolean error;
    private boolean enabled;
    private CustomEnchantDefinition definition;


    public CustomEnchantBuilder(String name) {
        this.error = false;
        this.namespacedName = Util.getNamedspacedName(name);
        FileConfiguration config = Files.ENCHANTMENTS.getConfig();

        //Parsing the enabled option
        enabled = config.getBoolean(namespacedName + ".enabled");
        if (!enabled) return;

        //Parsing the dependencies
        List<String> dependencies = config.getStringList(namespacedName + ".depends");
        Set<String> missingDependencies = findMissingDependencies(dependencies);
        if (!missingDependencies.isEmpty()) {
            Util.error(namespacedName + ": requires following missing dependencies: " + missingDependencies);
            error = true;
            return;
        }

        //Parsing the max level of the enchantment

        maxLvl = config.getInt(namespacedName + ".definition.max_level");
        if (maxLvl <= 0) {
            Util.error(namespacedName + ": 'max_level' must be greater than 0; using default value 1");
            maxLvl = 1;
        }

        //Parsing the definition
        definition = new CustomEnchantDefinitionBuilder(
                namespacedName,
                maxLvl,
                config.getConfigurationSection(namespacedName + ".definition")
        ).build();
        if (definition == null) error = true;

        //Parsing the custom locations for the enchanted item
        List<String> locations = config.getStringList(namespacedName + ".custom_locations");
        for (String location : locations) {
            try {
                customEnchantedItemLocations.add(CustomEnchantedItemLocation.valueOf(location.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                String closest = Util.findClosestMatch(location, CustomEnchantedItemLocation.class);
                String closest_message = closest == null ? "." : ", did you mean " + closest + "?";
                Util.warn(namespacedName + ": " + location.toUpperCase() + " is not a valid custom enchanted item location" + closest_message + " Skipping...");
            }
        }

        parseTriggers(config.getConfigurationSection(namespacedName + ".triggers"));
        if (triggers.isEmpty())
            Util.error(namespacedName + ": no triggers defined; this enchantment will never activate.");
    }

    public CustomEnchantBuilder(DefaultCustomEnchant defaultCustomEnchant) {
        this.namespacedName = defaultCustomEnchant.getNamespacedName();
        FileConfiguration config = Files.DEFAULT_ENCHANTMENTS.getConfig();

        if (!config.contains(this.namespacedName)) {
            Util.error("Default custom enchantment " + this.namespacedName + " has not been found in the default_enchantments.yml file");
            error = true;
            return;
        }

        //Parsing the enabled option
        enabled = config.getBoolean(namespacedName + ".enabled");
        if (!enabled) return;

        //Setting max level
        maxLvl = defaultCustomEnchant.getMaxLevel();

        //Parsing the definition
        definition = new CustomEnchantDefinitionBuilder(
                namespacedName,
                maxLvl,
                config.getConfigurationSection(namespacedName + ".definition")
        ).build();
        if (definition == null) error = true;

        //Register the listener
        Util.registerListener(defaultCustomEnchant.getListener());
    }

    private Set<String> findMissingDependencies(List<String> dependencies) {
        return dependencies.stream().filter(plugin -> Bukkit.getPluginManager().getPlugin(plugin) == null).collect(
                Collectors.toSet());
    }

    private void parseTriggers(ConfigurationSection triggerSection) {
        if (triggerSection == null) return;
        for (String triggerName : triggerSection.getKeys(false)) {
            ConfigurationSection triggerConfigSection = triggerSection.getConfigurationSection(triggerName);
            CustomEnchantTrigger trigger = new CustomEnchantTriggerBuilder(
                    namespacedName,
                    maxLvl,
                    triggerName,
                    triggerConfigSection
            ).build();
            if (trigger != null) triggers.add(trigger);
        }

        removeOverriddenTriggers();
    }

    private void removeOverriddenTriggers() {
        Iterator<CustomEnchantTrigger> it = this.triggers.iterator();
        while (it.hasNext()) {
            TriggerType type = it.next().getType();
            // Loop through all types that can override this one
            for (TriggerType overriddenByType : type.getOverriddenBy()) {
                if (triggers.stream().anyMatch(trigger -> trigger.getType() == overriddenByType)) {
                    // Remove the overridden trigger from the map
                    it.remove();
                    Util.warn(namespacedName + ": the '" + type + "' trigger was overridden by '" + overriddenByType + "', so it has been removed");
                }
            }
        }
    }


    public void build(boolean defaultEnchantment) {
        if (error)
            Util.error(namespacedName + ": error during building process, fix above errors before building again");
        else if (enabled) {
            CustomEnchant customEnchant = new CustomEnchant(
                    namespacedName,
                    defaultEnchantment,
                    definition,
                    customEnchantedItemLocations,
                    tags
            );
            triggers.forEach(trigger -> trigger.setCustomEnchant(customEnchant));
        }
    }
}
