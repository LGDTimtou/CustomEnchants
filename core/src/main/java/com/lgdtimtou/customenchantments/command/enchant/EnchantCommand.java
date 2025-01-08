package com.lgdtimtou.customenchantments.command.enchant;

import com.lgdtimtou.customenchantments.command.enchant.subcommands.SubCommandList;
import com.lgdtimtou.customenchantments.other.Util;
import com.lgdtimtou.customenchantments.command.Command;
import com.lgdtimtou.customenchantments.command.enchant.subcommands.EnchantSubCommand;
import com.lgdtimtou.customenchantments.command.enchant.subcommands.SubCommandAdd;
import com.lgdtimtou.customenchantments.command.enchant.subcommands.SubCommandRemove;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantCommand extends Command {

    public EnchantCommand() {
        super("customenchant", new SubCommandAdd(), new SubCommandRemove(), new SubCommandList());
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player player)){
            sender.sendMessage(Util.getMessage("OnlyPlayers"));
            return false;
        }
        if (args.length == 0){
            sender.sendMessage(Util.getMessage("EnchantCommandUsage"));
            return false;
        }

        EnchantSubCommand subCommand = (EnchantSubCommand) this.getSubCommand(args[0].toLowerCase());
        if (subCommand == null){
            sender.sendMessage(Util.getMessage("EnchantCommandUsage"));
            return false;
        }

        if (args.length < subCommand.getMinArguments()) {
            sender.sendMessage(Util.getMessage(subCommand.getUsageMessageID()));
            return false;
        }

        subCommand.execute(player, args);
        return true;
    }
}
