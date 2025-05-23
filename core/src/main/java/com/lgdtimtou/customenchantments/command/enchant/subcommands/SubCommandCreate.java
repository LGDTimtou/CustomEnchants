package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.command.Command;
import com.lgdtimtou.customenchantments.command.SubCommand;
import com.lgdtimtou.customenchantments.other.Message;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SubCommandCreate extends SubCommand {


    public SubCommandCreate(Command command) {
        super(command, "create", 0, Message.COMMANDS__CREATE__USAGE.get());
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Main.getWebSocketConnection().sendEnchantment(commandSender, null);
    }

    @Override
    public List<String> getTabValues(CommandSender commandSender, String[] args) {
        return List.of();
    }
}
