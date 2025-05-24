package be.timonc.customenchantments.other;

import org.bukkit.ChatColor;

import java.util.Map;

public enum Message {

    GLOBAL__PREFIX("&4&l[&6&lCustomEnchants&4&l]&r "),
    GLOBAL__SETTING("&cUpdate %setting% in config.yml to alter this behaviour"),
    GLOBAL__RESTART_SERVER("&7Please &orestart/reload the server &7to apply changes."),

    COMMANDS__USAGE("&7Usage: /ce <add | remove | create | edit | list>"),
    COMMANDS__ONLY_PLAYERS("&cOnly players can use this command"),
    COMMANDS__NO_PERMISSION("&cYou do not have permission to execute this command"),
    COMMANDS__NON_EXISTING_ENCHANT("&cEnchantment does not exist"),
    COMMANDS__EMPTY_HAND("&cYou must be holding an item"),


    COMMANDS__ADD__USAGE("&7Usage: /ce add <enchantment> [level]"),
    COMMANDS__ADD__ALREADY_HAS_ENCHANT("&cThis item is already enchanted with %enchant% %level%"),
    COMMANDS__ADD__LEVEL_RANGE("&cThe level of %enchant% should be in range: [1:%max_level%]"),
    COMMANDS__ADD__UNSAFE_ENCHANTMENT("&c%enchant% cannot be applied to %item%"),
    COMMANDS__ADD__CONFLICTING_ENCHANTMENT("&c%enchant% conflicts with %conflicting_enchantments%"),


    COMMANDS__REMOVE__USAGE("&7Usage: /ce remove <enchantment>"),
    COMMANDS__REMOVE__DOESNT_HAVE_ENCHANT("&cThis item is not enchanted with %enchant%"),

    COMMANDS__CREATE__USAGE("&7Usage: /ce create"),
    COMMANDS__CREATE__CONSOLE_MESSAGE("&7Visit this URL to create a new enchantment:\n&9%url%"),
    COMMANDS__CREATE__PLAYER_MESSAGE("&b&lClick Here &7to create a new enchantment"),
    COMMANDS__CREATE__PLAYER_MESSAGE_HOVER("&7Open the &bCustom Enchant Editor &7in your browser"),
    COMMANDS__CREATE__KICK(
            "&eYou have been kicked because a new enchantment has been added to the server. Please rejoin."),
    COMMANDS__CREATE__EXISTING_NAME("&e%existing% already existed. The new enchantment has been renamed to: %new%"),

    COMMANDS__EDIT__USAGE("&7Usage: /ce edit <enchantment>"),
    COMMANDS__EDIT__DEFAULT_ENCHANT(
            "&7You can configure default enchantment settings in the &edefault_enchantments.yml &7file located in the plugin folder."),
    COMMANDS__EDIT__CONSOLE_MESSAGE("&7Visit this URL to edit &b%enchant%&7:\n&9%url%"),
    COMMANDS__EDIT__PLAYER_MESSAGE("&b&lClick Here &7to edit &b%enchant%"),
    COMMANDS__EDIT__PLAYER_MESSAGE_HOVER("&7Open the &bCustom Enchant Editor &7in your browser"),
    COMMANDS__EDIT__NO_YAML(
            "&cThis enchantment is missing from enchantments.yml, did you rename it without restarting the server?"),
    COMMANDS__EDIT__NAME_CHANGE(
            "&eChanging an enchantment name is not safe! To continue, manually edit enchantments.yml and restart the server!"),

    COMMANDS__LIST__USAGE("&7Usage: /ce list"),

    COMMANDS__RELOAD__USAGE("&7Usage: /ce reload"),
    COMMANDS__RELOAD__SUCCESS("&aSuccessfully reloaded files!"),


    WEBSOCKET__CONNECTING("&7Connecting to websocket server..."),
    WEBSOCKET__SLOW_CONNECTION(
            "&7Connection is slow as the server is starting up. This should only take a minute. Thanks for your patience!"),
    WEBSOCKET__CONNECTED("&7WebSocket connected successfully!"),
    WEBSOCKET__SUCCESS("&aEnchantment successfully updated!"),
    WEBSOCKET__SESSION_EXPIRED("&eWebSocket connection expired after 30 minutes of inactivity"),

    WEBSOCKET__ERROR__YAML_PARSE("&cCould not parse the incoming YAML\n%yaml%"),
    WEBSOCKET__ERROR__REMOTE_OFFLINE("&cRemote server is offline"),
    WEBSOCKET__ERROR__REMOTE_CLOSED(
            "&eRemote server shutting down, restarting connection. Please wait before pushing changes as they may not be registered."),
    WEBSOCKET__ERROR__CLOSED_UNKNOWN_REASON("&cWebSocket connection closed due to reason: %reason% with code: %code%");


    private final String defaultMessage;

    Message(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefault() {
        return defaultMessage;
    }

    public String getPath() {
        return this.name().replace("__", ".").toLowerCase();
    }

    private String getMessage() {
        String message = File.MESSAGES.getConfig().getString(getPath());
        if (message == null) {
            Util.error("Message " + getPath() + " was not found in messages.yml, setting its default value");
            File.MESSAGES.getConfig().set(getPath(), defaultMessage);
            File.MESSAGES.save();
            return getMessage();
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private String getMessageParametersReplaced(Map<String, String> parameters) {
        return parameters.entrySet()
                         .stream()
                         .reduce(
                                 getMessage(),
                                 (message, entry) -> message.replace(
                                         "%" + entry.getKey() + "%",
                                         entry.getValue()
                                 ),
                                 (s1, s2) -> s1
                         );
    }

    public String get() {
        return get(Map.of());
    }

    public String getNoPrefix() {
        return getNoPrefix(Map.of());
    }


    public String get(Map<String, String> parameters) {
        return GLOBAL__PREFIX.getMessage() + getMessageParametersReplaced(parameters);
    }

    public String getNoPrefix(Map<String, String> parameters) {
        return getMessageParametersReplaced(parameters);
    }
}