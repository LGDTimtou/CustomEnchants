package com.lgdtimtou.customenchantments.other;

import com.google.common.collect.Sets;
import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Util {

    private static final Set<EnchantmentTarget> armorEnchantmentTargets = Set.of(EnchantmentTarget.ARMOR, EnchantmentTarget.ARMOR_FEET, EnchantmentTarget.ARMOR_LEGS, EnchantmentTarget.ARMOR_TORSO, EnchantmentTarget.ARMOR_HEAD);
    private static final Set<EquipmentSlot> armorEquipmentSlots = Set.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD);

    public static void registerListener(Listener listener){
        Main.getMain().getServer().getPluginManager().registerEvents(listener, Main.getMain());
    }

    public static void log(String message){
        Bukkit.getConsoleSender().sendMessage("[CustomEnchants] " + message);
    }

    public static void error(String message) {log(ChatColor.RED + "[ERROR]" + message);}
    public static void warn(String message) {log(ChatColor.YELLOW + "[WARNING]" + message);}

    public static String title(String text){
        return Arrays.stream(text.split(" ")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
    }

    public static String getMessage(String name){
        String message = Files.MESSAGES.getConfig().getString(name);
        if (name.equals("Prefix"))
            return message == null ? ChatColor.RED + "Prefix not found!" : format(message);
        return Util.getMessage("Prefix") +
                (message == null ? ChatColor.RED + name + " message not found!" : format(message));
    }

    public static String getMessageNoPrefix(String name){
        String message = Files.MESSAGES.getConfig().getString(name);
        return (message == null ? ChatColor.RED + name + " message not found!" : format(message));
    }

    public static String format(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static ItemStack getPlayerHead(UUID uuid){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getEnchantedItem(Set<ItemStack> customEnchantedItemsSelection, CustomEnchant customEnchant){
        for (ItemStack item : customEnchantedItemsSelection)
            if (containsEnchant(item, customEnchant.getEnchantment())) return item;
        return null;
    }

    public static ItemStack getEnchantedItem(PlayerInventory inventory, CustomEnchant customEnchant) {
        Set<EquipmentSlot> slots = targetsToSlots(customEnchant.getEnchantmentTargets());
        for (EquipmentSlot slot : slots) {
            ItemStack item = inventory.getItem(slot);
            if (containsEnchant(item, customEnchant.getEnchantment())) return item;
        }
        return null;
    }

    public static boolean containsEnchant(ItemStack item, Enchantment enchantment){
        if (item == null) return false;
        return item.containsEnchantment(enchantment);
    }

    public static int getLevel(ItemStack item, Enchantment enchantment){
        if (item == null)
            return -1;
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet())
            if (entry.getKey().getKey().equals(enchantment.getKey()))
                return entry.getValue();
        return -1;
    }
    public static <E extends Enum<E>, A> Collection<E> filterEnumNames(Stream<String> stream, Class<E> enumClass, Collector<E, A, Collection<E>> collector){
        return stream
                .filter(tr -> Arrays.stream(enumClass.getEnumConstants()).anyMatch(v -> v.name().equals(tr.toUpperCase())))
                .map(tr -> E.valueOf(enumClass, tr.toUpperCase())).collect(collector);
    }

    public static <E extends Enum<E>> Map<E, Set<String>> filterMap(Map<String, Object> map, Class<E> enumClass){
        Map<E, Set<String>> result = new HashMap<>();
        HashSet<E> enums = (HashSet<E>) filterEnumNames(map.keySet().stream(), enumClass, Collectors.toCollection(HashSet::new));
        for (E enumValue : enums){
            Object obj = map.get(enumValue.name().toLowerCase());
            if (obj instanceof String)
                result.put(enumValue, Util.yamlListToStream((String) obj).collect(Collectors.toSet()));
            else if (obj instanceof ArrayList){
                HashSet<String> set = new HashSet<>();
                for (Object item : (ArrayList<?>) obj)
                    set.add(String.valueOf(item));
                result.put(enumValue, set);
            }
        }
        return result;
    }

    public static Stream<String> yamlListToStream(String yamlList){
        if (yamlList == null) return Stream.empty();
        return Arrays.stream(yamlList.replaceAll("^\\[", "").replaceAll("]$", "").split("[ ]*,[ ]*"));
    }

    public static String replaceParameters(Map<String, String> parameters, String str){
        /* Oneliner
        return parameters.entrySet().stream()
                .reduce(str, (result, entry) -> result.replaceAll(entry.getKey(), entry.getValue()),
                        (a, b) -> a);
        */
        String result = str;
        for (Map.Entry<String, String> mapEntry : parameters.entrySet())
            result = result.replaceAll("%" + mapEntry.getKey() + "%", mapEntry.getValue());
        return result;
    }

    public static String secondsToString(int seconds, boolean full){
        int hours = Math.floorDiv(seconds, 3600);
        int minutes = Math.floorDiv(seconds % 3600, 60);
        int sec = seconds % 60;
        return (hours > 0 ? hours + (full ? " hours " : "h "):"") + (minutes > 0 ? minutes + (full ? " minutes " : "m "):"") + sec + (full ? " seconds":"s");
    }
    public static String getPrettyName(String namespacedName) {
        return Util.title(namespacedName.replaceAll("_", " "));
    }


    public static Set<EquipmentSlot> targetsToSlots(Set<EnchantmentTarget> targets) {
        Set<EquipmentSlot> slots = new HashSet<>();

        if (Files.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean()) {
            slots.addAll(armorEquipmentSlots);
            slots.add(EquipmentSlot.HAND);
            return slots;
        }

        for (EnchantmentTarget target : targets){
            if (armorEnchantmentTargets.contains(target))
                slots.addAll(armorEquipmentSlots);
            else
                slots.add(EquipmentSlot.HAND);
        }
        return slots;
    }

    public static String getNamedspacedName(String name) {
        return name.toLowerCase().replaceAll(" ", "_");
    }


    public static Set<Material> targetsToMats(Set<EnchantmentTarget> targets) {
        Set<Material> materials = new HashSet<>();
        targets.forEach(enchantmentTarget -> materials.addAll(targetToMats(enchantmentTarget)));
        return materials;
    }
    private static Set<Material> targetToMats(EnchantmentTarget target) {
        return switch(target) {
            case ARMOR_FEET -> Set.of(Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS, Material.GOLDEN_BOOTS, Material.NETHERITE_BOOTS);
            case ARMOR_LEGS -> Set.of(Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.NETHERITE_LEGGINGS);
            case ARMOR_TORSO -> Set.of(Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.NETHERITE_CHESTPLATE);
            case ARMOR_HEAD -> Set.of(Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.TURTLE_HELMET, Material.NETHERITE_HELMET);
            case ARMOR -> Sets.union(Sets.union(targetToMats(EnchantmentTarget.ARMOR_FEET), targetToMats(EnchantmentTarget.ARMOR_LEGS)), Sets.union(targetToMats(EnchantmentTarget.ARMOR_TORSO), targetToMats(EnchantmentTarget.ARMOR_HEAD)));
            case TOOL -> Set.of(Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.DIAMOND_SHOVEL, Material.GOLDEN_SHOVEL, Material.NETHERITE_SHOVEL, Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.GOLDEN_PICKAXE, Material.NETHERITE_PICKAXE, Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.GOLDEN_AXE, Material.NETHERITE_AXE, Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.DIAMOND_HOE, Material.GOLDEN_HOE, Material.NETHERITE_HOE);
            case WEAPON -> Set.of(Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.GOLDEN_SWORD, Material.NETHERITE_SWORD);
            case WEARABLE -> Sets.union(targetToMats(EnchantmentTarget.ARMOR), Set.of(Material.ELYTRA, Material.CARVED_PUMPKIN, Material.SKELETON_SKULL, Material.WITHER_SKELETON_SKULL, Material.ZOMBIE_HEAD, Material.PIGLIN_HEAD, Material.PLAYER_HEAD, Material.CREEPER_HEAD, Material.DRAGON_HEAD, Material.SHIELD));
            case BOW -> Set.of(Material.BOW);
            case CROSSBOW -> Set.of(Material.CROSSBOW);
            case TRIDENT -> Set.of(Material.TRIDENT);
            case FISHING_ROD -> Set.of(Material.FISHING_ROD);
            case VANISHABLE, BREAKABLE, ALL -> Arrays.stream(Material.values()).collect(Collectors.toSet());
        };
    }

    public static Enchantment getEnchantmentByName(String name) {
        NamespacedKey namespacedKey = new NamespacedKey("minecraft", getNamedspacedName(name));
        return Enchantment.getByKey(namespacedKey);
    }

}
