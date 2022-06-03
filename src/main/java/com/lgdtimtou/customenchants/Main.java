package com.lgdtimtou.customenchants;

import com.lgdtimtou.customenchants.command.Command;
import com.lgdtimtou.customenchants.command.enchant.EnchantCommand;
import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import com.lgdtimtou.customenchants.enchantments.listeners.Replenish;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


//TODO KillCounter enchant
//TODO extend command to /ce add|remove enchant level


public final class Main extends JavaPlugin {

    private static Main plugin;


    @Override
    public void onEnable() {
        plugin = this;
        CustomEnchant.register();
        Files.register();


        Command enchantCommand = new EnchantCommand();

        Util.log(ChatColor.GREEN + "Successfully enabled Timtou's plugin");
    }

    @Override
    public void onDisable() {
        Util.log(ChatColor.GREEN + "Successfully disabled Timtou's plugin");
    }


    public static Main getMain(){
        return plugin;
    }



}
