package com.lgdtimtou.customenchantments.command.enchant;

import com.lgdtimtou.customenchantments.command.Command;
import com.lgdtimtou.customenchantments.command.enchant.subcommands.*;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.command.CommandSender;

public class EnchantCommand extends Command {

    public EnchantCommand() {
        super(
                "customenchant",
                new SubCommandAdd(),
                new SubCommandRemove(),
                new SubCommandEdit(),
                new SubCommandList(),
                new SubCommandCreate()
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Util.getMessage("EnchantCommandUsage"));
            return false;
        }

        EnchantSubCommand subCommand = (EnchantSubCommand) this.getSubCommand(args[0].toLowerCase());
        if (subCommand == null) {
            sender.sendMessage(Util.getMessage("EnchantCommandUsage"));
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
