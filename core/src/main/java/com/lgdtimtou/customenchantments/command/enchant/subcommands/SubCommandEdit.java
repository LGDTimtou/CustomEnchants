package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.command.Command;
import com.lgdtimtou.customenchantments.command.SubCommand;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.other.Files;
import com.lgdtimtou.customenchantments.other.Util;
import com.lgdtimtou.customenchantments.other.WebSocketConnection;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.stream.Collectors;

public class SubCommandEdit extends SubCommand {

    public SubCommandEdit(Command command) {
        super(command, "edit", 2, "EnchantSubCommandEditUsage");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        String enchantmentName = args[1].toLowerCase();

        //Enchant bestaat niet
        CustomEnchant customEnchant;
        try {
            customEnchant = CustomEnchant.get(enchantmentName);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(Util.getMessage("NonExistingEnchant"));
            return;
        }

        // Default enchantments
        if (customEnchant.isDefaultEnchantment()) {
            commandSender.sendMessage(Util.getMessage("EditDefaultEnchant"));
            return;
        }

        String yaml = getYamlForEnchant(customEnchant);
        if (yaml == null) {
            commandSender.sendMessage(Util.getMessage("EditorNoYaml"));
            return;
        }

        WebSocketConnection webSocketConnection = WebSocketConnection.get();
        webSocketConnection.sendEnchantment(commandSender, customEnchant.getNamespacedName(), yaml);

        //EditorWebSocketClient client = EditorWebSocketClient.editingEnchantments.getOrDefault(commandSender, Map.of())
        //                                                                        .getOrDefault(customEnchant, null);
        //if (client == null) {
        //    client = new EditorWebSocketClient(commandSender, customEnchant, yaml);
        //    EditorWebSocketClient.editingEnchantments.putIfAbsent(commandSender, new HashMap<>());
        //    EditorWebSocketClient.editingEnchantments.get(commandSender).put(customEnchant, client);
        //    client.connect();
        //} else client.sendURL(commandSender);
    }

    @Override
    public List<String> getTabValues(CommandSender commandSender, String[] args) {
        if (args.length == 2)
            return CustomEnchant.getCustomEnchantSet().stream()
                                .filter(ce -> !ce.isDefaultEnchantment())
                                .filter(ce -> ce.getName()
                                                .toLowerCase()
                                                .startsWith(args[1].toLowerCase()))
                                .map(CustomEnchant::getNamespacedName)
                                .collect(Collectors.toList());
        else return null;
    }

    private String getYamlForEnchant(CustomEnchant customEnchant) {
        String enchantmentName = customEnchant.getNamespacedName();
        ConfigurationSection section = Files.ENCHANTMENTS.getConfig().getConfigurationSection(enchantmentName);
        if (section == null) return null;

        YamlConfiguration fullYaml = new YamlConfiguration();
        YamlConfiguration inner = new YamlConfiguration();

        for (String key : section.getKeys(false)) {
            inner.set(key, section.get(key));
        }

        fullYaml.set(enchantmentName, inner);
        return fullYaml.saveToString();
    }
}
