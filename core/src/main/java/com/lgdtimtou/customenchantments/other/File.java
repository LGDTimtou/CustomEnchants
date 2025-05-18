package com.lgdtimtou.customenchantments.other;

import com.lgdtimtou.customenchantments.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

public enum File {

    CONFIG("config.yml", (file) -> {}),
    ENCHANTMENTS("enchantments.yml", (file) -> {}),
    DEFAULT_ENCHANTMENTS("default_enchantments.yml", (file) -> {}),
    WS(".ws.yml", (file) -> {}),
    MESSAGES("messages.yml", (file) -> {
        for (Message message : Message.values())
            if (file.getConfig().get(message.getPath()) == null)
                file.getConfig().set(message.getPath(), message.getDefault());
        file.save();
    });

    private final String path;
    private final Consumer<File> initialization;
    private FileConfiguration config;
    private java.io.File file;


    File(String path, Consumer<File> initialization) {
        this.path = path;
        this.initialization = initialization;
    }


    public static void register() {
        Arrays.stream(File.values()).forEach(File::registerFile);
    }

    private void registerFile() {
        if (file == null)
            file = new java.io.File(Main.getMain().getDataFolder(), path);
        if (!file.exists() || file.length() == 0)
            Main.getMain().saveResource(path, true);
        initialization.accept(this);
    }

    public void reloadConfig() {
        if (file == null)
            file = new java.io.File(Main.getMain().getDataFolder(), path);

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
            return File.CONFIG.getConfig().getBoolean(this.path);
        }
    }
}
