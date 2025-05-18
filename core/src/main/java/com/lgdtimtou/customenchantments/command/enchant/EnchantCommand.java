package com.lgdtimtou.customenchantments.command.enchant;

import com.lgdtimtou.customenchantments.command.Command;
import com.lgdtimtou.customenchantments.command.SubCommand;
import com.lgdtimtou.customenchantments.command.enchant.subcommands.*;
import com.lgdtimtou.customenchantments.other.Util;
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
            sender.sendMessage(Util.getMessage("NoPermission"));
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(Util.getMessage("EnchantCommandUsage"));
            return false;
        }

        SubCommand subCommand = this.getSubCommand(args[0].toLowerCase());
        if (subCommand == null) {
            sender.sendMessage(Util.getMessage("EnchantCommandUsage"));
            return false;
        }

        if (!subCommand.hasPermission(sender)) {
            sender.sendMessage(Util.getMessage("NoPermission"));
            return false;
        }

        if (args.length < subCommand.getMinArguments()) {
            sender.sendMessage(Util.getMessage(subCommand.getUsageMessageID()));
            return false;
        }

        subCommand.execute(sender, args);
        return true;
    }
}
