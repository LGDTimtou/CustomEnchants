package com.lgdtimtou.customenchantments.other;

import com.lgdtimtou.customenchantments.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

public class WebSocketConnection extends WebSocketClient {

    private static final String debugUrl = "ws://localhost:8000/ws/plugin/";
    private static final String url = "wss://timonc-backend.onrender.com/ws/plugin/";
    private static WebSocketConnection webSocketConnection;

    private boolean connecting = false;
    private BukkitTask sendLongConnectionTimeMessage;

    public WebSocketConnection() {
        super(buildUri());

        if (getConnectionPersistentState())
            connect();
    }

    public static void register() {
        webSocketConnection = new WebSocketConnection();
    }

    public static WebSocketConnection get() {
        return webSocketConnection;
    }


    private static URI buildUri() {
        try {
            // TODO: debug url naar url veranderen
            String url = debugUrl + "?server_id=" + getServerID();
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static String getServerID() {
        String path = "ws_server_id";
        return getSecretOrDefault(path);
    }

    @NotNull
    private static String getEnchantmentSecret(String enchantNamespacedName) {
        String path = "ws_enchantment_secrets." + enchantNamespacedName;
        return getSecretOrDefault(path);
    }

    @NotNull
    private static String getSecretOrDefault(String path) {
        String secret = Files.WS.getConfig().getString(path, null);
        if (secret == null || secret.isBlank()) {
            secret = UUID.randomUUID().toString();
            Files.WS.getConfig().set(path, secret);
            Files.WS.save();
        }
        return secret;
    }


    public void connect(CommandSender commandSender) {
        if (!getConnectionPersistentState())
            setConnectionPersistentState(true);

        if (connecting) {
            commandSender.sendMessage(Util.getMessage("EditorConnectingSlow"));
            return;
        }

        if (commandSender != null) {
            commandSender.sendMessage(Util.getMessage("EditorConnecting"));

            this.sendLongConnectionTimeMessage = Bukkit.getScheduler().runTaskLater(
                    Main.getMain(),
                    () -> commandSender.sendMessage(Util.getMessage("EditorConnectingSlow")),
                    100
            );
        }

        connecting = true;
        connect();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        connecting = false;
        this.sendLongConnectionTimeMessage.cancel();
        Util.log("Websocket connection setup!");
    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }

    public void sendEnchantment(CommandSender commandSender, String enchantNamespacedName, String yaml) {
        if (!isOpen())
            connect(commandSender);

        JSONObject payload = new JSONObject(Map.of(
                "action", "save_enchantment",
                "secret", getEnchantmentSecret(enchantNamespacedName),
                "yaml", yaml == null ? "" : yaml
        ));
        send(payload.toString());
    }

    private boolean getConnectionPersistentState() {
        return Files.WS.getConfig().getBoolean("connected", false);
    }

    private void setConnectionPersistentState(boolean value) {
        Files.WS.getConfig().set("connected", value);
        Files.WS.save();
    }
}
