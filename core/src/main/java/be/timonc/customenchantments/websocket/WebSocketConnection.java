package be.timonc.customenchantments.websocket;

import be.timonc.customenchantments.Main;
import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.other.File;
import be.timonc.customenchantments.other.Message;
import be.timonc.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class WebSocketConnection extends WebSocketClient {

    private static final Set<BukkitTask> tasks = new HashSet<>();
    private static final String debugUrl = "ws://localhost:8000/ws/plugin/";
    private static final String wsURL = "wss://timonc-backend.onrender.com/ws/plugin/";

    private boolean connecting = false;

    public WebSocketConnection() {
        super(buildUri());

        WebSocketSession.loadPersistentWebSocketSessions();
        if (!WebSocketSession.getWebSocketSessions().isEmpty())
            connect();
    }


    private static URI buildUri() {
        try {
            String url = wsURL + "?server_id=" + getServerID();
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static String getServerID() {
        String path = "ws_server_id";
        String secret = File.WS.getConfig().getString(path, null);
        if (secret == null || secret.isBlank()) {
            secret = UUID.randomUUID().toString();
            File.WS.getConfig().set(path, secret);
            File.WS.save();
        }
        return secret;
    }

    @Override
    public void connect() {
        connecting = true;
        super.connect();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        connecting = false;
        cancelTasks();
        WebSocketSession.sendDistinctMessage(Message.WEBSOCKET__CONNECTED.get());
        WebSocketSession.getWebSocketSessions().forEach(this::sendEnchantmentYAML);
    }

    @Override
    public void onMessage(String message) {
        JSONObject payload = new JSONObject(message);
        if (payload.has("type")) {
            String type = payload.get("type").toString();
            if (type.equalsIgnoreCase("update_yaml") && payload.has("secret") && payload.has("yaml")) {
                String secret = payload.get("secret").toString();
                String yaml = payload.get("yaml").toString();
                WebSocketSession.get(secret).updateYAML(yaml);
                return;
            }
        }

        Util.error("Received a wrongly formatted message!" + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        cancelTasks();
        CloseReason closeReason = Arrays.stream(CloseReason.values())
                                        .filter(cr -> cr.code == code)
                                        .findFirst()
                                        .orElse(null);

        if (closeReason != null && closeReason != CloseReason.SERVER_SHUTDOWN)
            Arrays.stream(closeReason.messages)
                  .forEach(WebSocketSession::sendDistinctMessage);
        else if (closeReason != CloseReason.SERVER_SHUTDOWN) {
            WebSocketSession.sendDistinctMessage(Message.WEBSOCKET__ERROR__CLOSED_UNKNOWN_REASON.get(Map.of(
                    "reason", reason,
                    "code", String.valueOf(code)
            )));
        }

        // Reset the stored websocket connection and save all sessions
        Main.resetWebSocketConnection();
        WebSocketSession.savePersistentWebSocketSessions();
        if (closeReason == null || closeReason.clearSessions) {
            File.WS.getConfig().set("ws_sessions", List.of());
            File.WS.save();
        }

        // If the remote server closed we call the websocket connection so it opens the connection again
        if (closeReason == CloseReason.REMOTE_SERVER_CLOSED)
            Main.getWebSocketConnection();
    }

    @Override
    public void onError(Exception e) {
        Util.debug("WebSocket error: " + e.getMessage());
    }

    public void shutdown() {
        close(CloseReason.SERVER_SHUTDOWN.code);
    }


    public void sendEnchantment(CommandSender commandSender, CustomEnchant customEnchant) {
        if (customEnchant != null && customEnchant.getYaml() == null) {
            commandSender.sendMessage(Message.COMMANDS__EDIT__NO_YAML.get());
            return;
        }

        WebSocketSession session = WebSocketSession.get(commandSender, customEnchant);
        session.setStatus(WebSocketSessionStatus.WAITING_CONNECTION_URL);

        if (connecting) {
            commandSender.sendMessage(Message.WEBSOCKET__CONNECTING.get());
            return;
        }

        if (!isOpen()) {
            commandSender.sendMessage(Message.WEBSOCKET__CONNECTING.get());
            tasks.add(Bukkit.getScheduler()
                            .runTaskLater(
                                    Main.getMain(),
                                    () -> commandSender.sendMessage(Message.WEBSOCKET__SLOW_CONNECTION.get()),
                                    100L
                            ));
            connect();
        } else sendEnchantmentYAML(session);
    }

    private void sendEnchantmentYAML(WebSocketSession session) {
        if (isOpen()) {
            send(session.getJson().toString());
            if (session.getStatus() == WebSocketSessionStatus.WAITING_CONNECTION_URL)
                session.sendURL();
            session.setStatus(WebSocketSessionStatus.ACCEPTING_RESPONSE);
        } else {
            Util.error("Trying to send enchantment yaml with no open connection. Report this!");
        }
    }

    private void cancelTasks() {
        tasks.forEach(BukkitTask::cancel);
        tasks.clear();
    }

    public enum CloseReason {
        REMOTE_SERVER_CLOSED(1006, false, Message.WEBSOCKET__ERROR__REMOTE_CLOSED.get()),
        SERVER_SHUTDOWN(4998, false, ""),
        SESSION_EXPIRED(4999, Message.WEBSOCKET__SESSION_EXPIRED.get()),
        REMOTE_SERVER_OFFLINE(-1, Message.WEBSOCKET__ERROR__REMOTE_CLOSED.get());

        private final int code;
        private final boolean clearSessions;
        private final String[] messages;

        CloseReason(int code, String... messages) {
            this.code = code;
            this.clearSessions = true;
            this.messages = messages;
        }

        CloseReason(int code, boolean clearSessions, String... messages) {
            this.code = code;
            this.clearSessions = clearSessions;
            this.messages = messages;
        }
    }
}
