package be.timonc.customenchantments.command.enchant;

import be.timonc.customenchantments.command.Command;
import be.timonc.customenchantments.command.SubCommand;
import be.timonc.customenchantments.command.enchant.subcommands.*;
import be.timonc.customenchantments.other.Message;
import org.bukkit.command.CommandSender;

public class EnchantCommand extends Command {

    public EnchantCommand() {
        super("customenchant");
        setSubCommands(
                new SubCommandAdd(this),
                new SubCommandRemove(this),
                new SubCommandEdit(this),
                new SubCommandList(this),
                new SubCommandCreate(this),
                new SubCommandReload(this)
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!hasPermission(sender)) {
            sender.sendMessage(Message.COMMANDS__NO_PERMISSION.get());
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(Message.COMMANDS__USAGE.get());
            return false;
        }

        SubCommand subCommand = this.getSubCommand(args[0].toLowerCase());
        if (subCommand == null) {
            sender.sendMessage(Message.COMMANDS__USAGE.get());
            return false;
        }

        if (!subCommand.hasPermission(sender)) {
            sender.sendMessage(Message.COMMANDS__NO_PERMISSION.get());
            return false;
        }

        if (args.length < subCommand.getMinArguments()) {
            sender.sendMessage(subCommand.getUsageMessage());
            return false;
        }

        subCommand.execute(sender, args);
        return true;
    }
}
