package com.lgdtimtou.customenchantments.other;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class EditorWebSocketClient extends WebSocketClient {

    public static final Map<CommandSender, Map<CustomEnchant, EditorWebSocketClient>> editingEnchantments = new HashMap<>();
    public static final Map<CommandSender, EditorWebSocketClient> creatingEnchantments = new HashMap<>();
    private static final Set<EditorWebSocketClient> connections = new HashSet<>();

    private static final String url = "wss://timonc-backend.onrender.com/ws/plugin/";
    private final CustomEnchant customEnchant;
    private final String yaml;
    private final BukkitTask runnable;
    private CommandSender commandSender;
    private String secret;

    public EditorWebSocketClient(CommandSender commandSender, CustomEnchant customEnchant, String yaml) {
        super(buildUri());
        this.commandSender = commandSender;
        this.customEnchant = customEnchant;
        this.yaml = yaml;

        connections.add(this);
        sendMessage(Util.getMessage("EditorConnecting"));

        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isOpen())
                    sendMessage(Util.getMessage("EditorConnectingSlow"));
            }
        }.runTaskLater(Main.getMain(), 100);
    }

    private static URI buildUri() {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void shutdown() {
        for (EditorWebSocketClient client : connections) {
            try {
                client.close(CloseReason.SERVER_SHUTDOWN);
            } catch (Exception e) {
                Util.error("Error when closing websocket connection!" + e.getMessage());
            }
        }
        connections.clear();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        JSONObject payload = new JSONObject(Map.of(
                "action", "start_session",
                "yaml", yaml == null ? "" : yaml
        ));
        send(payload.toString());
    }

    @Override
    public void onMessage(String message) {
        JSONObject payload = new JSONObject(message);
        if (payload.has("type")) {
            String type = payload.get("type").toString();
            if (type.equalsIgnoreCase("session_started") && payload.has("secret")) {
                this.secret = payload.get("secret").toString();
                sendURL(commandSender);
                return;
            } else if (type.equalsIgnoreCase("update_yaml") && payload.has("yaml")) {
                String yaml = payload.get("yaml").toString();
                Util.debug("Received yaml: " + yaml);
                YamlConfiguration yamlConf = new YamlConfiguration();
                try {
                    yamlConf.loadFromString(yaml);
                } catch (Exception e) {
                    close(CloseReason.COULD_NOT_PARSE_YAML);
                    return;
                }
                createEnchantment(yamlConf);
                return;
            }
        }

        close(CloseReason.WRONG_FORMATTED_MESSAGE);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        editingEnchantments.getOrDefault(commandSender, new HashMap<>()).remove(customEnchant);
        creatingEnchantments.put(commandSender, null);
        connections.remove(this);
        runnable.cancel();
        CloseReason closeReason = Arrays.stream(CloseReason.values())
                                        .filter(cr -> cr.code == code)
                                        .findFirst()
                                        .orElse(null);
        if (closeReason != null)
            Arrays.stream(closeReason.messages).forEach(this::sendMessage);
        else {
            Util.debug("Unkown closereason was found with code: " + code + ", reason: " + reason);
            sendMessage(Util.getMessage("EditorDisconnected"));
        }
    }

    @Override
    public void onError(Exception e) {
        Util.debug("Error occurred on websocket connection: " + e.getMessage());
    }

    private void sendMessage(String message) {
        if (commandSender instanceof Player player && !player.isOnline())
            return;

        this.commandSender.sendMessage(message);
    }

    public void sendURL(CommandSender commandSender) {
        this.commandSender = commandSender;
        String url = Util.getWebBuilderUrl("secret", secret);

        if (commandSender instanceof Player player && player.isOnline()) {
            String message = customEnchant == null ? Util.getMessage("EditorClickToCreate") : Util.getMessage(
                                                                                                          "EditorClickToEdit")
                                                                                                  .replace(
                                                                                                          "%enchant%",
                                                                                                          customEnchant.getName()
                                                                                                  );
            TextComponent openEditor = new TextComponent(message);
            openEditor.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
            openEditor.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new Text(Util.getMessageNoPrefix("EditorURLHover"))
            ));
            player.spigot().sendMessage(openEditor);
        } else {
            String message = customEnchant == null ? Util.getMessage("EditorURLConsoleCreate") : Util.getMessage(
                                                                                                             "EditorURLConsoleEdit")
                                                                                                     .replace(
                                                                                                             "%enchant%",
                                                                                                             customEnchant.getName()
                                                                                                     );
            sendMessage(message);
        }
    }

    private void createEnchantment(YamlConfiguration yaml) {
        String newName = yaml.getKeys(false).stream().findFirst().orElse(null);
        if (newName == null) {
            close(CloseReason.COULD_NOT_PARSE_YAML);
            return;
        }

        boolean isNewName = customEnchant != null && !newName.equals(customEnchant.getNamespacedName());
        if (isNewName)
            sendMessage(Util.getMessage("EditorCannotChangeName"));

        String duplicateName = newName;
        int i = 1;
        while (customEnchant == null && Files.ENCHANTMENTS.getConfig().get(duplicateName) != null)
            duplicateName = newName + i++;

        if (!duplicateName.equalsIgnoreCase(newName))
            sendMessage(Util.getMessage("EditorDuplicateName")
                            .replace("%existing%", newName)
                            .replace("%new%", duplicateName));

        String name = isNewName ? customEnchant.getNamespacedName() : duplicateName;
        Object yamlContent = yaml.get(newName);

        Files.ENCHANTMENTS.getConfig().set(name, yamlContent);
        Files.ENCHANTMENTS.save();
        close(CloseReason.SUCCESS);
    }

    private void close(CloseReason closeReason) {
        close(closeReason.code);
    }

    public enum CloseReason {
        WRONG_FORMATTED_MESSAGE(4000, Util.getMessage("EditorInvalidMessage")),
        SERVER_SHUTDOWN(4001, Util.getMessage("EditorServerShutdown")),
        SUCCESS(4002, Util.getMessage("EditorYAMLSuccess"), Util.getMessage("EditorRestartServer")),
        COULD_NOT_PARSE_YAML(4003, Util.getMessage("EditorYAMLParseFail")),
        SESSION_EXPIRED(4999, Util.getMessage("EditorSessionExpired")),
        REMOTE_SERVER_CLOSED(1006, Util.getMessage("EditorRemoteClosed")),
        REMOTE_SERVER_OFFLINE(-1, Util.getMessage("EditorRemoteOffline"));

        private final int code;
        private final String[] messages;

        CloseReason(int code, String... messages) {
            this.code = code;
            this.messages = messages;
        }
    }
}