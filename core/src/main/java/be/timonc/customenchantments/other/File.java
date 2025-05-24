package be.timonc.customenchantments.other;

import be.timonc.customenchantments.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

public enum File {

    CONFIG("config.yml", (file) -> {
        Arrays.stream(ConfigValue.values()).forEach(ConfigValue::check);
        file.save();
    }),
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

        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Util.error("Error when parsing " + name() + ": " + e.getMessage());
            if (e instanceof InvalidConfigurationException)
                Util.error("Remember to wrap strings in (single) quotation marks '<string>' or \"<string>\"");
        }
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

        VERBOSE("verbose", false),
        ALLOW_UNSAFE_ENCHANTMENTS("allow_unsafe_enchantments", false),
        PLAYER_IDLE_TRIGGER_CHECK_FREQUENCY("player_idle_trigger_check_frequency", 0.5);

        private final String path;
        private final Object defaultValue;

        ConfigValue(String path, Object defaultValue) {
            this.path = path;
            this.defaultValue = defaultValue;
        }

        public Boolean getBoolean() {
            return File.CONFIG.getConfig().getBoolean(this.path);
        }

        public Double getDouble() {
            return File.CONFIG.getConfig().getDouble(this.path);
        }

        public void check() {
            if (File.CONFIG.getConfig().get(path) == null)
                File.CONFIG.getConfig().set(path, defaultValue);
        }
    }
}
