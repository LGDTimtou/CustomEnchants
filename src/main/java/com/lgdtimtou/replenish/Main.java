package com.lgdtimtou.replenish;

import com.lgdtimtou.replenish.command.EnchantCommand;
import com.lgdtimtou.replenish.command.EnchantCommandTab;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;


//TODO can enchant with same enchant if level is different


public final class Main extends JavaPlugin {

    private static Main plugin;


    @Override
    public void onEnable() {
        plugin = this;
        CustomEnchant.register();
        Files.register();

        PluginCommand command = this.getCommand("customenchant");
        assert(command != null);
        command.setExecutor(new EnchantCommand());
        command.setTabCompleter(new EnchantCommandTab());

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
