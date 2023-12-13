package com.lgdtimtou.customenchants;

import com.lgdtimtou.customenchants.command.enchant.EnchantCommand;
import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import com.lgdtimtou.customenchants.other.Files;
import com.lgdtimtou.customenchants.other.Util;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

//TODO KillCounter enchant
//TODO make the enchantments work on books


public final class Main extends JavaPlugin {

    private static Main plugin;


    @Override
    public void onEnable() {
        plugin = this;
        Files.register();
        CustomEnchant.register();
        new EnchantCommand();
    }

    @Override
    public void onDisable() {}


    public static Main getMain(){
        return plugin;
    }



}
