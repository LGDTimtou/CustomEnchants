package com.lgdtimtou.customenchantments.other;

import com.lgdtimtou.customenchantments.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum Files {

    CONFIG("config.yml"),
    MESSAGES("messages.yml"),
    ENCHANTMENTS("enchantments.yml"),
    DEFAULT_ENCHANTMENTS("default_enchantments.yml"),
    WS(".ws.yml");

    private final String path;

    private FileConfiguration config;
    private File file;


    Files(String path) {
        this.path = path;
    }

    public static void register() {
        for (Files yamlFile : Files.values()) {
            if (yamlFile.file == null)
                yamlFile.file = new File(Main.getMain().getDataFolder(), yamlFile.path);
            if (!yamlFile.file.exists())
                Main.getMain().saveResource(yamlFile.path, false);
        }
    }

    public void reloadConfig() {
        if (file == null)
            file = new File(Main.getMain().getDataFolder(), path);

        config = YamlConfiguration.loadConfiguration(file);
        save();
    }

    public FileConfiguration getConfig() {
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
            Util.error("Could not save file: " + path);
        }
    }


    public enum ConfigValue {

        VERBOSE("verbose"),
        ALLOW_UNSAFE_ENCHANTMENTS("allow_unsafe_enchantments");

        private final String path;

        ConfigValue(String path) {
            this.path = path;
        }

        public boolean getBoolean() {
            return Files.CONFIG.getConfig().getBoolean(this.path);
        }
    }
}
