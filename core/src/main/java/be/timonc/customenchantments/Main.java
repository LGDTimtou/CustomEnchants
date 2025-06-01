package be.timonc.customenchantments;

import be.timonc.customenchantments.command.enchant.EnchantCommand;
import be.timonc.customenchantments.customevents.CustomEvent;
import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.enchantments.created.fields.instructions.InstructionCleanupListeners;
import be.timonc.customenchantments.nms.EnchantmentManager;
import be.timonc.customenchantments.other.File;
import be.timonc.customenchantments.other.Util;
import be.timonc.customenchantments.websocket.WebSocketConnection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class Main extends JavaPlugin {


    private static Main plugin;
    private static boolean PAPISupport;
    private static EnchantmentManager enchantmentsManager;
    private static WebSocketConnection webSocketConnection;
    private static String minecraftVersion;

    public static Main getMain() {
        return plugin;
    }

    public static boolean isPAPISupport() {
        return PAPISupport;
    }

    public static boolean isFirstBoot() {
        return System.getProperty("RELOAD") == null;
    }

    public static EnchantmentManager getEnchantmentsManager() {
        return enchantmentsManager;
    }

    public static WebSocketConnection getWebSocketConnection() {
        if (webSocketConnection == null)
            webSocketConnection = new WebSocketConnection();
        return webSocketConnection;
    }

    public static void resetWebSocketConnection() {
        webSocketConnection = null;
    }

    private static void shutdownWebSocketConnection() {
        if (webSocketConnection != null)
            webSocketConnection.shutdown();
    }


    private static void setPAPISupport() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            PAPISupport = true;
        else Util.warn("PlaceholderAPI is not installed. PAPI placeholders will not be replaced.");
    }

    @Override
    public void onEnable() {
        plugin = this;
        if (!isFirstBoot())
            Util.warn("Reloading will not load all changes made to an enchantment. Use /restart to apply them properly.");

        // Register and load the files
        File.register();

        setPAPISupport();

        createNMSClasses();
        if (enchantmentsManager == null) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        CustomEnchant.register();
        CustomEvent.register();

        new EnchantCommand();

        Util.registerListener(new InstructionCleanupListeners());

        // Creating the WebSocketConnection
        webSocketConnection = new WebSocketConnection();
    }

    @Override
    public void onDisable() {
        System.setProperty("RELOAD", "TRUE");
        shutdownWebSocketConnection();
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
    private void createNMSClasses() throws RuntimeException {
        String baseClazzName = "be.timonc.customenchantments.nms_" + getMinecraftVersion()
                .replace(".", "_") + ".";
        try {
            Class<? extends EnchantmentManager> enchantmentManagerClass = (Class<? extends EnchantmentManager>) Class.forName(
                    baseClazzName + "EnchantmentManagerImpl");

            enchantmentsManager = enchantmentManagerClass.getConstructor().newInstance();
        } catch (ClassNotFoundException exception) {
            Util.error("Minecraft " + getMinecraftVersion() +
                    " is not supported by this version of CustomEnchantments)");
            Util.error("Download our latest update for newer versions!");
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
