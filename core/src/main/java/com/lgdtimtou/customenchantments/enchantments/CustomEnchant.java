package com.lgdtimtou.customenchantments.enchantments;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.enchantments.created.builders.CustomEnchantBuilder;
import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantedItemLocation;
import com.lgdtimtou.customenchantments.enchantments.defaultenchants.DefaultCustomEnchant;
import com.lgdtimtou.customenchantments.other.File;
import com.lgdtimtou.customenchantments.other.Message;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import java.util.*;


public class CustomEnchant extends CustomEnchantRecord {

    private static final String[] roman = new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    private static final Map<String, CustomEnchant> enchantments = new HashMap<>();

    private final boolean defaultEnchantment;
    private final List<CustomEnchantedItemLocation> customEnchantedItemLocations;
    private Enchantment enchantment;

    private boolean newlyRegistered = false;


    public CustomEnchant(String name, boolean defaultEnchantment, CustomEnchantDefinition definition, List<CustomEnchantedItemLocation> customEnchantedItemLocations, Map<String, Boolean> tags) {
        super(Util.getPrettyName(name), definition, tags);

        this.defaultEnchantment = defaultEnchantment;
        this.customEnchantedItemLocations = customEnchantedItemLocations;

        this.enchantment = Util.getEnchantmentByName(this.getNamespacedName());
        if (Main.isFirstBoot() || this.enchantment == null) {
            this.enchantment = Main.getEnchantmentsManager().registerEnchantment(this);
            newlyRegistered = true;

            if (!Main.isFirstBoot())
                for (Player player : Bukkit.getOnlinePlayers())
                    player.kickPlayer(Message.COMMANDS__CREATE__KICK.getNoPrefix());
        }

        enchantments.put(this.namespacedName, this);
    }

    //Registering
    public static void register() {
        Main.getEnchantmentsManager().unFreezeRegistry();

        //Build CustomEnchantments from enchantments.yml
        for (String enchant : File.ENCHANTMENTS.getConfig().getConfigurationSection("").getValues(false).keySet())
            new CustomEnchantBuilder(enchant).build(false);

        //Register the default custom enchantments
        for (DefaultCustomEnchant defaultCustomEnchant : DefaultCustomEnchant.values())
            new CustomEnchantBuilder(defaultCustomEnchant).build(true);

        for (CustomEnchant customEnchant : getCustomEnchantSet())
            if (customEnchant.isNewlyRegistered())
                Main.getEnchantmentsManager()
                    .addExclusives(customEnchant.getNamespacedName(), customEnchant.getConflictingEnchantments());

        Main.getEnchantmentsManager().freezeRegistry();

        Util.log("Registered enchantments: " + getCustomEnchantSet().stream()
                                                                    .map(CustomEnchant::getNamespacedName)
                                                                    .toList());
    }

    public static CustomEnchant get(String name) {
        if (!enchantments.containsKey(name))
            throw new IllegalArgumentException(name + " enchantment does not exist");
        return enchantments.get(name);
    }

    //Info for each level

    public static Set<CustomEnchant> getCustomEnchantSet() {
        return new HashSet<>(enchantments.values());
    }

    public static String getLevelRoman(int level) {
        if (level < 1 || level > roman.length)
            return "error";
        return roman[level - 1];
    }

    public boolean hasPermission(Permissible permissible) {
        return !needPermission() || Util.hasPermission(permissible, "enchantment." + getNamespacedName());
    }

    private boolean isNewlyRegistered() {
        return newlyRegistered;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public boolean isDefaultEnchantment() {
        return defaultEnchantment;
    }

    public ItemStack getEnchantedItem(Player player, Set<ItemStack> priorityItems) {
        //Handling priority items
        if (!priorityItems.isEmpty()) {
            Util.debug("Priority Items: " + priorityItems);
            for (ItemStack item : priorityItems)
                if (item.containsEnchantment(this.enchantment)) return item;
            return null;
        }

        // Handle manually selected item locations
        if (!customEnchantedItemLocations.isEmpty()) {
            List<ItemStack> customSelectedItems = customEnchantedItemLocations.stream()
                                                                              .flatMap(loc -> loc.getCustomEnchantedItems(
                                                                                      player).stream())
                                                                              .filter(Objects::nonNull)
                                                                              .filter(item -> item.containsEnchantment(
                                                                                      this.enchantment))
                                                                              .toList();

            if (!customSelectedItems.isEmpty()) {
                Util.debug("Custom Enchanted Items found in " + customEnchantedItemLocations + ": " + customSelectedItems);
                return customSelectedItems.getFirst();
            }

            return null;
        }

        // Fallback: default item
        return Util.getEnchantedItem(player, this);
    }

    public String getYaml() {
        ConfigurationSection section = File.ENCHANTMENTS.getConfig().getConfigurationSection(getNamespacedName());
        if (section == null) return null;

        YamlConfiguration fullYaml = new YamlConfiguration();
        YamlConfiguration inner = new YamlConfiguration();

        for (String key : section.getKeys(false)) {
            inner.set(key, section.get(key));
        }

        fullYaml.set(getNamespacedName(), inner);
        return fullYaml.saveToString();
    }
}
