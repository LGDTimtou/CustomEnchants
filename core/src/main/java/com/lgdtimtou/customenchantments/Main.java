package com.lgdtimtou.customenchantments;

import com.lgdtimtou.customenchantments.command.enchant.EnchantCommand;
import com.lgdtimtou.customenchantments.customevents.CustomEvent;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.nms.EnchantmentManager;
import com.lgdtimtou.customenchantments.other.Files;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class Main extends JavaPlugin {

    private static Main plugin;
    private static EnchantmentManager enchantmentsManager;
    private static String minecraftVersion;

    public static Main getMain() {
        return plugin;
    }

    public static boolean isFirstBoot() {
        return System.getProperty("RELOAD") == null;
    }

    public static EnchantmentManager getEnchantmentsManager() {
        return enchantmentsManager;
    }

    @Override
    public void onEnable() {
        if (!isFirstBoot())
            Util.warn("Reloading this plugin WILL severely break its vanilla functionality. Use /restart instead!");
        plugin = this;
        Files.register();

        enchantmentsManager = createEnchantmentManager();
        if (enchantmentsManager == null) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        CustomEnchant.register();
        CustomEvent.register();
        new EnchantCommand();
    }

    @Override
    public void onDisable() {
        System.setProperty("RELOAD", "TRUE");
    }

    private String getMinecraftVersion() {
        if (minecraftVersion != null) {
            return minecraftVersion;
        } else {
            String bukkitGetVersionOutput = Bukkit.getVersion();
            Matcher matcher = Pattern.compile("\\(MC: (?<version>[\\d]+\\.[\\d]+(\\.[\\d]+)?)\\)")
                                     .matcher(bukkitGetVersionOutput);
            if (matcher.find())
                return minecraftVersion = matcher.group("version");
            else
                throw new RuntimeException("Could not determine Minecraft version from Bukkit.getVersion(): " + bukkitGetVersionOutput);
        }
    }

    @SuppressWarnings("unchecked")
    private EnchantmentManager createEnchantmentManager() throws RuntimeException {
        String clazzName = "com.lgdtimtou.customenchantments.nms_" + getMinecraftVersion()
                .replace(".", "_") + ".EnchantmentManagerImpl";
        try {
            Class<? extends EnchantmentManager> clazz = (Class<? extends EnchantmentManager>) Class.forName(clazzName);
            return clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException exception) {
            Util.error("Minecraft " + getMinecraftVersion() +
                    " is not supported by this version of CustomEnchantments)");
            Util.error("Download our latest update for newer versions!");
            return null;
        } catch (ReflectiveOperationException exception) {
            return null;
        }
    }
}
