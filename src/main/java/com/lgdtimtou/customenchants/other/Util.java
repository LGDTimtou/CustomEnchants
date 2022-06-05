package com.lgdtimtou.customenchants.other;

import com.lgdtimtou.customenchants.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Util {

    public static void registerEvent(Listener listener){
        Main.getMain().getServer().getPluginManager().registerEvents(listener, Main.getMain());
    }

    public static void log(String message){
        Bukkit.getLogger().info(message);
    }

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
}
