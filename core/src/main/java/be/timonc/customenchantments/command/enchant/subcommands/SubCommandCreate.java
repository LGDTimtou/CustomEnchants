package be.timonc.customenchantments.command.enchant.subcommands;

import be.timonc.customenchantments.Main;
import be.timonc.customenchantments.command.Command;
import be.timonc.customenchantments.command.SubCommand;
import be.timonc.customenchantments.other.Message;
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
