package com.lgdtimtou.customenchantments.websocket;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.other.Files;
import com.lgdtimtou.customenchantments.other.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class WebSocketSession {

    private static final Set<WebSocketSession> webSocketSessions = new HashSet<>();

    private WebSocketSessionStatus status = WebSocketSessionStatus.WAITING_CONNECTION;
    private WebSocketSessionType type;
    private String secret;
    private CommandSender commandSender;
    private String customEnchantName;
    private String yaml;

    public WebSocketSession(CommandSender commandSender, CustomEnchant customEnchant) {
        this.type = customEnchant == null ? WebSocketSessionType.CREATE : WebSocketSessionType.EDIT;
        this.secret = UUID.randomUUID().toString();
        this.commandSender = commandSender;
        this.customEnchantName = customEnchant == null ? "" : customEnchant.getNamespacedName();
        this.yaml = customEnchant == null ? "" : customEnchant.getYaml();
        webSocketSessions.add(this);
        saveState();
    }


    public WebSocketSession(Map<?, ?> persistentSession) {
        try {
            type = WebSocketSessionType.valueOf(persistentSession.get("type").toString());
            String commandSenderString = persistentSession.get("command_sender").toString();
            commandSender = commandSenderString.equalsIgnoreCase("console") ? Bukkit.getConsoleSender() : Bukkit.getPlayer(
                    commandSenderString);

            String customEnchantString = persistentSession.get("enchant").toString();
            customEnchantName = type == WebSocketSessionType.CREATE ? "" : customEnchantString;
            yaml = type == WebSocketSessionType.CREATE ? "" : CustomEnchant.get(customEnchantString).getYaml();

            secret = persistentSession.get("secret").toString();
            webSocketSessions.add(this);
        } catch (Exception e) {
            Util.error("Couldn't load WebSocket Session: " + persistentSession + "\nError: " + e.getMessage());
        }
    }

    public static Set<WebSocketSession> getWebSocketSessions() {
        return webSocketSessions;
    }

    public static void loadPersistentWebSocketSessions() {
        for (Map<?, ?> session : Files.WS.getConfig().getMapList("ws_sessions"))
            webSocketSessions.add(new WebSocketSession(session));
    }

    public static void savePersistentWebSocketSessions() {
        webSocketSessions.forEach(WebSocketSession::saveState);
        webSocketSessions.clear();
    }

    public static WebSocketSession get(String secret) {
        return webSocketSessions.stream().filter(session -> session.secret.equals(secret)).findFirst().orElse(null);
    }

    @NotNull
    public static WebSocketSession get(CommandSender commandSender, CustomEnchant customEnchant) {
        WebSocketSession session = webSocketSessions.stream()
                                                    .filter(s ->
                                                            s.commandSender.equals(commandSender) &&
                                                                    s.customEnchantName.equals(customEnchant == null ? "" : customEnchant.getNamespacedName()))
                                                    .findFirst()
                                                    .orElse(null);

        if (session == null)
            session = new WebSocketSession(commandSender, customEnchant);
        return session;
    }

    private static void sendMessage(CommandSender commandSender, String message) {
        if (commandSender instanceof Player player && !player.isOnline())
            return;

        commandSender.sendMessage(message);
    }

    public static void sendDistinctMessage(String message) {
        webSocketSessions.stream().map(session -> session.commandSender).collect(Collectors.toSet()).forEach(
                commandSender -> sendMessage(commandSender, message)
        );
    }

    private void saveState() {
        List<Map<?, ?>> sessions = Files.WS.getConfig().getMapList("ws_sessions");

        Map<String, Object> sessionData = Map.of(
                "type", type.name(),
                "secret", secret,
                "command_sender", commandSender.getName(),
                "enchant", customEnchantName
        );

        int index = 0;
        while (index < sessions.size()) {
            if (sessions.get(index).get("secret").equals(secret)) {
                sessions.set(index, sessionData);
                break;
            }
            index++;
        }

        if (index == sessions.size())
            sessions.add(sessionData);

        Files.WS.getConfig().set("ws_sessions", sessions);
        Files.WS.save();
    }

    public void updateYAML(String yamlString) {
        YamlConfiguration yamlConf = new YamlConfiguration();
        try {
            yamlConf.loadFromString(yamlString);
        } catch (Exception e) {
            sendMessage(Util.getMessage("EditorYAMLParseFail").replace("%yaml%", yamlString));
            return;
        }

        String newName = yamlConf.getKeys(false).stream().findFirst().orElse(null);
        if (newName == null) {
            sendMessage(Util.getMessage("EditorYAMLParseFail").replace("%yaml%", yamlString));
            return;
        }

        boolean isNewName = type == WebSocketSessionType.EDIT && !newName.equals(customEnchantName);
        if (isNewName)
            sendMessage(Util.getMessage("EditorCannotChangeName"));

        String duplicateName = newName;
        int i = 1;
        while (type == WebSocketSessionType.CREATE && Files.ENCHANTMENTS.getConfig().get(duplicateName) != null)
            duplicateName = newName + i++;

        if (!duplicateName.equalsIgnoreCase(newName))
            sendMessage(Util.getMessage("EditorDuplicateName")
                            .replace("%existing%", newName)
                            .replace("%new%", duplicateName));

        String name = isNewName ? customEnchantName : duplicateName;
        Object yamlContent = yamlConf.get(newName);

        Files.ENCHANTMENTS.getConfig().set(name, yamlContent);
        Files.ENCHANTMENTS.save();

        this.type = WebSocketSessionType.EDIT;
        this.customEnchantName = name;
        saveState();

        sendMessage(Util.getMessage("EditorYAMLSuccess"));
        sendMessage(Util.getMessage("EditorRestartServer"));
    }

    public WebSocketSessionStatus getStatus() {
        return this.status;
    }

    public void setStatus(WebSocketSessionStatus status) {
        this.status = status;
    }

    private void sendMessage(String message) {
        sendMessage(commandSender, message);
    }

    public void sendURL() {
        String url = Util.getWebBuilderUrl("secret", secret);

        if (commandSender instanceof Player player) {
            String message = type == WebSocketSessionType.CREATE ? Util.getMessage("EditorClickToCreate") : Util.getMessage(
                                                                                                                        "EditorClickToEdit")
                                                                                                                .replace(
                                                                                                                        "%enchant%",
                                                                                                                        customEnchantName
                                                                                                                );
            TextComponent openEditor = new TextComponent(message);
            openEditor.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
            openEditor.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new Text(Util.getMessageNoPrefix("EditorURLHover"))
            ));
            player.spigot().sendMessage(openEditor);
        } else {
            String message = type == WebSocketSessionType.CREATE ?
                    Util.getMessage("EditorURLConsoleCreate") :
                    Util.getMessage("EditorURLConsoleEdit")
                        .replace(
                                "%enchant%",
                                customEnchantName
                        );

            sendMessage(message.replace(
                    "%url%",
                    url
            ));
        }
    }

    public JSONObject getJson() {
        return new JSONObject(Map.of(
                "action", "save_enchantment",
                "secret", secret,
                "yaml", yaml
        ));
    }

    enum WebSocketSessionType {
        EDIT,
        CREATE
    }
}
