package com.lgdtimtou.customenchantments.enchantments;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.enchantments.created.CustomEnchantBuilder;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.defaultenchants.DefaultCustomEnchant;
import com.lgdtimtou.customenchantments.other.Files;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class CustomEnchant extends CustomEnchantRecord {

    private static final String[] roman = new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    private static final Map<String, CustomEnchant> enchantments = new HashMap<>();

    private final boolean defaultEnchantment;
    private final List<CustomEnchantedItemLocation> customEnchantedItemLocations;
    private final List<CustomEnchantBuilder.CustomEnchantLevel> levels;
    private final Map<EnchantTriggerType, Map<ConditionKey, Set<String>>> registeredTriggers;
    private final Enchantment enchantment;


    public CustomEnchant(String name, boolean defaultEnchantment, CustomEnchantDefinition definition, List<CustomEnchantedItemLocation> customEnchantedItemLocations, Map<String, Boolean> tags, String coolDownMessage, List<CustomEnchantBuilder.CustomEnchantLevel> levels, Map<EnchantTriggerType, Map<ConditionKey, Set<String>>> registeredTriggers) {
        super(Util.getPrettyName(name), definition, tags, coolDownMessage);

        this.defaultEnchantment = defaultEnchantment;
        this.customEnchantedItemLocations = customEnchantedItemLocations;
        this.levels = levels;
        this.registeredTriggers = registeredTriggers;

        if (Main.isFirstBoot())
            this.enchantment = Main.getEnchantmentsManager().registerEnchantment(this);
        else
            this.enchantment = Util.getEnchantmentByName(this.namespacedName);

        if (this.enchantment == null) {
            Util.error("Failed to register enchantment: " + this.namespacedName);
            Util.error(
                    "Avoid using /reload when adding new enchantments or modifying vanilla behavior — restart the server instead!");
            return;
        }

        registeredTriggers.keySet().forEach(type -> Util.registerListener(type.getTrigger(this)));
        enchantments.put(this.namespacedName, this);
    }

    //Registering
    public static void register() {
        if (Main.isFirstBoot())
            registerFirstBoot();
        else
            registerReload();
    }

    //Info for each level

    private static void registerFirstBoot() {
        Main.getEnchantmentsManager().unFreezeRegistry();

        //Build CustomEnchantments from enchantments.yml
        for (String enchant : Files.ENCHANTMENTS.getConfig().getConfigurationSection("").getValues(false).keySet())
            new CustomEnchantBuilder(enchant).build(false);

        //Register the default custom enchantments
        for (DefaultCustomEnchant defaultCustomEnchant : DefaultCustomEnchant.values())
            new CustomEnchantBuilder(defaultCustomEnchant).build(true);

        //Register the conflicting enchantments
        for (CustomEnchant customEnchant : getCustomEnchantSet())
            Main.getEnchantmentsManager()
                .addExclusives(customEnchant.getNamespacedName(), customEnchant.getConflictingEnchantments());

        Main.getEnchantmentsManager().freezeRegistry();
        Util.log("Registered enchantments: " + getCustomEnchantSet().stream()
                                                                    .map(CustomEnchant::getNamespacedName)
                                                                    .toList());
    }

    private static void registerReload() {
        //Build CustomEnchantments from enchantments.yml
        for (String enchant : Files.ENCHANTMENTS.getConfig().getConfigurationSection("").getValues(false).keySet())
            new CustomEnchantBuilder(enchant).build(false);

        //Register the default custom enchantments
        for (DefaultCustomEnchant defaultCustomEnchant : DefaultCustomEnchant.values())
            new CustomEnchantBuilder(defaultCustomEnchant).build(true);

        Util.log("Registered enchantments: " + getCustomEnchantSet().stream()
                                                                    .map(CustomEnchant::getNamespacedName)
                                                                    .toList());
    }

    public static CustomEnchant get(String name) {
        if (!enchantments.containsKey(name))
            throw new IllegalArgumentException(name + " enchantment does not exist");
        return enchantments.get(name);
    }

    public static Set<CustomEnchant> getCustomEnchantSet() {
        return new HashSet<>(enchantments.values());
    }

    public static String getLevelRoman(int level) {
        if (level < 1 || level > roman.length)
            return "error";
        return roman[level - 1];
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public boolean isDefaultEnchantment() {
        return defaultEnchantment;
    }

    public int getCooldown(int level) {
        if (level <= 0 || level > levels.size())
            return -1;
        return levels.get(level - 1).getCooldown();
    }

    public int getChance(int level) {
        if (level <= 0 || level > levels.size())
            return -1;
        return (int) levels.get(level - 1).getChance();
    }

    public boolean isCancelled(int level) {
        if (level <= 0 || level > levels.size())
            return false;
        return levels.get(level - 1).isEventCancelled();
    }

    public List<String> getCommands(int level) {
        if (level <= 0 || level > levels.size())
            return Collections.emptyList();
        return levels.get(level - 1).getCommands();
    }

    public boolean checkTriggerConditions(EnchantTriggerType type, Map<ConditionKey, Object> triggerConditionMap) {
        registeredTriggers.get(type).forEach((conditionKey, strings) -> {
            if (!triggerConditionMap.containsKey(conditionKey))
                Util.warn(namespacedName + ": " + conditionKey.toString()
                                                              .toUpperCase() + " is not a valid condition for " + type);
        });

        for (Map.Entry<ConditionKey, Object> entry : triggerConditionMap.entrySet()) {
            ConditionKey conditionKey = entry.getKey();
            Object triggerObject = entry.getValue();
            Set<String> conditions = registeredTriggers.get(type).get(conditionKey);
            if (conditions != null)
                for (String condition : conditions)
                    if (!conditionKey.type().checkCondition(triggerObject, condition)) {
                        Util.debug(conditionKey + ":" + triggerObject + ", did not match any of the following " + conditions);
                        return false;
                    }
        }
        return true;
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

    public Map<EnchantTriggerType, Map<ConditionKey, Set<String>>> getRegisteredTriggers() {
        return registeredTriggers;
    }
}
