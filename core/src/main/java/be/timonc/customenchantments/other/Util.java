package be.timonc.customenchantments.other;

import be.timonc.customenchantments.Main;
import be.timonc.customenchantments.enchantments.CustomEnchant;
import com.google.common.collect.Sets;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Util {

    private static final Set<EnchantmentTarget> armorEnchantmentTargets = Set.of(
            EnchantmentTarget.ARMOR,
            EnchantmentTarget.ARMOR_FEET,
            EnchantmentTarget.ARMOR_LEGS,
            EnchantmentTarget.ARMOR_TORSO,
            EnchantmentTarget.ARMOR_HEAD
    );
    private static final Set<EquipmentSlot> armorEquipmentSlots = Set.of(
            EquipmentSlot.FEET,
            EquipmentSlot.LEGS,
            EquipmentSlot.CHEST,
            EquipmentSlot.HEAD
    );
    private static final Pattern placeholderPattern = Pattern.compile("%[a-z_]+%");
    private static final Pattern PARAM_PATTERN = Pattern.compile("%([a-zA-Z0-9_]+)%");

    public static boolean hasPermission(Permissible permissible, String permission) {
        return permissible.hasPermission("customenchantments." + permission) || permissible.hasPermission("ce." + permission);
    }

    public static String getWebBuilderUrl(String query, String value) {
        return "https://www.timonc.be/custom_enchants/custom_enchant_builder?" + query + "=" + value;
    }

    public static void registerListener(Listener listener) {
        Main.getMain().getServer().getPluginManager().registerEvents(listener, Main.getMain());
    }

    public static void log(String message) {
        Bukkit.getLogger().info("[CustomEnchants] " + message);
    }

    public static void error(String message) {
        Bukkit.getLogger().log(Level.SEVERE, "[CustomEnchants] " + message);
    }

    public static void warn(String message) {
        Bukkit.getLogger().warning("[CustomEnchants] " + message);
    }

    public static void debug(String message) {
        if (File.ConfigValue.VERBOSE.getBoolean())
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[DEBUG] " + message);
    }

    public static String title(String text) {
        return Arrays.stream(text.split(" "))
                     .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                     .collect(Collectors.joining(" "));
    }

    public static <E extends Enum<E>> String findClosestMatch(String input, Class<E> enumClass) {
        LevenshteinDistance distance = new LevenshteinDistance();
        String closest = null;
        int minDistance = Integer.MAX_VALUE;

        for (E constant : enumClass.getEnumConstants()) {
            String valid = constant.name();
            int dist = distance.apply(input.toUpperCase(), valid);
            if (dist < minDistance) {
                minDistance = dist;
                closest = valid;
            }
        }

        return (minDistance <= 3) ? closest : null;
    }

    public static int getLevel(ItemStack item, Enchantment enchantment) {
        if (item == null)
            return -1;
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet())
            if (entry.getKey().getKey().equals(enchantment.getKey()))
                return entry.getValue();
        return -1;
    }


    public static String replaceParameters(Player player, String value, Map<String, Supplier<String>> parameters) {
        Matcher paramMatcher = PARAM_PATTERN.matcher(value);
        StringBuilder resultBuffer = new StringBuilder();

        while (paramMatcher.find()) {
            String paramName = paramMatcher.group(1);
            String replacement = parameters.getOrDefault(paramName, () -> paramMatcher.group(0)).get();
            replacement = Matcher.quoteReplacement(replacement);
            paramMatcher.appendReplacement(resultBuffer, replacement);
        }
        paramMatcher.appendTail(resultBuffer);
        String result = resultBuffer.toString();

        if (Main.isPAPISupport())
            result = PlaceholderAPI.setPlaceholders(player, result);

        // Check for unresolved parameters
        Matcher finalMatcher = placeholderPattern.matcher(result);
        if (finalMatcher.find())
            Util.warn("Unresolved placeholders found in text: " + result);

        return result;
    }

    public static String secondsToString(int seconds, boolean full) {
        int hours = Math.floorDiv(seconds, 3600);
        int minutes = Math.floorDiv(seconds % 3600, 60);
        int sec = seconds % 60;
        return (hours > 0 ? hours + (full ? " hours " : "h ") : "") + (minutes > 0 ? minutes + (full ? " minutes " : "m ") : "") + sec + (full ? " seconds" : "s");
    }

    public static String getPrettyName(String namespacedName) {
        return Util.title(namespacedName.replaceAll("_", " "));
    }

    public static Set<EquipmentSlot> targetsToSlots(Set<EnchantmentTarget> targets) {
        Set<EquipmentSlot> slots = new HashSet<>();

        if (File.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean()) {
            slots.addAll(armorEquipmentSlots);
            slots.add(EquipmentSlot.HAND);
            return slots;
        }

        for (EnchantmentTarget target : targets) {
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

    public static ItemStack getEnchantedItem(Player player, CustomEnchant customEnchant) {
        Set<EquipmentSlot> validEquipmentSlots = Set.of(
                EquipmentSlot.HAND,
                EquipmentSlot.FEET,
                EquipmentSlot.LEGS,
                EquipmentSlot.CHEST,
                EquipmentSlot.HEAD
        );
        for (EquipmentSlot slot : validEquipmentSlots) {
            ItemStack item = player.getInventory().getItem(slot);
            if (item != null && item.containsEnchantment(customEnchant.getEnchantment())) {
                Util.debug("Found Item in default equipmentslot " + validEquipmentSlots + ": " + item);
                return item;
            }
        }
        return null;
    }

    public static String getCombinedString(String delimiter, String... strings) {
        return Arrays.stream(strings)
                     .filter(s -> s != null && !s.isEmpty())
                     .collect(Collectors.joining(delimiter));
    }

    private static Set<Material> targetToMats(EnchantmentTarget target) {
        return switch (target) {
            case ARMOR_FEET -> Set.of(
                    Material.LEATHER_BOOTS,
                    Material.CHAINMAIL_BOOTS,
                    Material.IRON_BOOTS,
                    Material.DIAMOND_BOOTS,
                    Material.GOLDEN_BOOTS,
                    Material.NETHERITE_BOOTS
            );
            case ARMOR_LEGS -> Set.of(
                    Material.LEATHER_LEGGINGS,
                    Material.CHAINMAIL_LEGGINGS,
                    Material.IRON_LEGGINGS,
                    Material.DIAMOND_LEGGINGS,
                    Material.GOLDEN_LEGGINGS,
                    Material.NETHERITE_LEGGINGS
            );
            case ARMOR_TORSO -> Set.of(
                    Material.LEATHER_CHESTPLATE,
                    Material.CHAINMAIL_CHESTPLATE,
                    Material.IRON_CHESTPLATE,
                    Material.DIAMOND_CHESTPLATE,
                    Material.GOLDEN_CHESTPLATE,
                    Material.NETHERITE_CHESTPLATE
            );
            case ARMOR_HEAD -> Set.of(
                    Material.LEATHER_HELMET,
                    Material.CHAINMAIL_HELMET,
                    Material.DIAMOND_HELMET,
                    Material.IRON_HELMET,
                    Material.GOLDEN_HELMET,
                    Material.TURTLE_HELMET,
                    Material.NETHERITE_HELMET
            );
            case ARMOR -> Sets.union(
                    Sets.union(
                            targetToMats(EnchantmentTarget.ARMOR_FEET),
                            targetToMats(EnchantmentTarget.ARMOR_LEGS)
                    ),
                    Sets.union(targetToMats(EnchantmentTarget.ARMOR_TORSO), targetToMats(EnchantmentTarget.ARMOR_HEAD))
            );
            case TOOL -> Set.of(
                    Material.WOODEN_SHOVEL,
                    Material.STONE_SHOVEL,
                    Material.IRON_SHOVEL,
                    Material.DIAMOND_SHOVEL,
                    Material.GOLDEN_SHOVEL,
                    Material.NETHERITE_SHOVEL,
                    Material.WOODEN_PICKAXE,
                    Material.STONE_PICKAXE,
                    Material.IRON_PICKAXE,
                    Material.DIAMOND_PICKAXE,
                    Material.GOLDEN_PICKAXE,
                    Material.NETHERITE_PICKAXE,
                    Material.WOODEN_AXE,
                    Material.STONE_AXE,
                    Material.IRON_AXE,
                    Material.DIAMOND_AXE,
                    Material.GOLDEN_AXE,
                    Material.NETHERITE_AXE,
                    Material.WOODEN_HOE,
                    Material.STONE_HOE,
                    Material.IRON_HOE,
                    Material.DIAMOND_HOE,
                    Material.GOLDEN_HOE,
                    Material.NETHERITE_HOE
            );
            case WEAPON -> Set.of(
                    Material.WOODEN_SWORD,
                    Material.STONE_SWORD,
                    Material.IRON_SWORD,
                    Material.DIAMOND_SWORD,
                    Material.GOLDEN_SWORD,
                    Material.NETHERITE_SWORD
            );
            case WEARABLE -> Sets.union(
                    targetToMats(EnchantmentTarget.ARMOR),
                    Set.of(
                            Material.ELYTRA,
                            Material.CARVED_PUMPKIN,
                            Material.SKELETON_SKULL,
                            Material.WITHER_SKELETON_SKULL,
                            Material.ZOMBIE_HEAD,
                            Material.PIGLIN_HEAD,
                            Material.PLAYER_HEAD,
                            Material.CREEPER_HEAD,
                            Material.DRAGON_HEAD,
                            Material.SHIELD
                    )
            );
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

    public enum TimeOfDay {
        DAY(0, 11999),
        NIGHT(12000, 23999),
        SUNRISE(0, 1000),
        SUNSET(12000, 13000);


        private final long min;
        private final long max;

        TimeOfDay(long min, long max) {
            this.min = min;
            this.max = max;
        }

        public static Set<TimeOfDay> fromTicks(long time) {
            return Arrays.stream(values())
                         .filter((val) -> val.min <= time && time <= val.max)
                         .collect(Collectors.toSet());
        }

        public static TimeOfDay dayOrNight(long time) {
            return time < 12000 ? DAY : NIGHT;
        }
    }
}
