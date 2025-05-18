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
            if (!yamlFile.file.exists() || yamlFile.file.length() == 0)
                Main.getMain().saveResource(yamlFile.path, true);
        }
    }

    public void reloadConfig() {
        if (file == null)
            file = new File(Main.getMain().getDataFolder(), path);

        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        if (config == null)
            reloadConfig();
        return config;
    }

    public void save() {
        if (config == null || file == null) {
            Util.debug("Could not save: " + this.name() + ", because the config or file is null");
            return;
        }
        try {
            getConfig().save(file);
        } catch (IOException e) {
            Util.debug("Could not save file: " + path);
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
