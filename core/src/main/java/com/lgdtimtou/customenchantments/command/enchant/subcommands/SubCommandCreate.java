package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.other.EditorWebSocketClient;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SubCommandCreate extends EnchantSubCommand {


    public SubCommandCreate() {
        super("create", 1, "EnchantSubCommandCreateUsage");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        EditorWebSocketClient client = EditorWebSocketClient.creatingEnchantments.getOrDefault(commandSender, null);
        if (client == null) {
            client = new EditorWebSocketClient(commandSender, null, null);
            EditorWebSocketClient.creatingEnchantments.put(commandSender, client);
            client.connect();
        } else client.sendURL(commandSender);
    }

    @Override
    public List<String> getTabValues(CommandSender sender, String[] args) {
        return null;
    }
}
