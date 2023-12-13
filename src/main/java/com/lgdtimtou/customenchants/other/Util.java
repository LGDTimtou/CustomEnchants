package com.lgdtimtou.customenchants.other;

import com.lgdtimtou.customenchants.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

    public static void registerListener(Listener listener){
        Main.getMain().getServer().getPluginManager().registerEvents(listener, Main.getMain());
    }

    public static void log(String message){
        Bukkit.getConsoleSender().sendMessage("[CustomEnchants] " + message);
    }

    public static void error(String message) {log(ChatColor.RED + message);}

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

    public static ItemStack getEnchantmentContainingItem(PlayerInventory inventory, Set<ItemStack> customEnchantedItemsSelection, Enchantment enchantment){
        if (customEnchantedItemsSelection.isEmpty()){
            for (EquipmentSlot slot : EquipmentSlot.values()){
                ItemStack item = inventory.getItem(slot);
                if (containsEnchant(item, enchantment)) return item;
            }
            return null;
        }
        for (ItemStack item : customEnchantedItemsSelection)
            if (containsEnchant(item, enchantment)) return item;
        return null;
    }

    public static boolean containsEnchant(ItemStack item, Enchantment enchantment){
        if (item == null) return false;
        return item.getEnchantments().keySet().stream().anyMatch(en -> en.getKey().equals(enchantment.getKey()));
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
        return Arrays.stream(yamlList.replaceAll("^\\[", "").replaceAll("]$", "").split("[ ]*,[ ]*"));
    }

}
