package com.lgdtimtou.customenchants;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public enum Files {

    MESSAGES("messages.yml");

    private final String path;

    private FileConfiguration config;
    private File file;


    Files(String path){
        this.path = path;
    }

    public void reloadConfig(){
        if (file == null)
            file = new File(Main.getMain().getDataFolder(), path);

        config = YamlConfiguration.loadConfiguration(file);

        InputStream defaultStream = Main.getMain().getResource(path);
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            config.setDefaults(defaultConfig);
        }
        save();
    }

    public FileConfiguration getConfig(){
        if (config == null)
            reloadConfig();
        return config;
    }

    public void save() {
        if (config == null || file == null)
            return;
        try {
            getConfig().save(file);
        } catch (IOException e) {
            Util.log(ChatColor.RED + "Could not save file: " + path);
        }
    }

    public static void register(){
        for (Files yamlFile : Files.values()){
            if (yamlFile.file == null)
                yamlFile.file = new File(Main.getMain().getDataFolder(), yamlFile.path);
            if (!yamlFile.file.exists())
                Main.getMain().saveResource(yamlFile.path, false);
        }
    }
}
