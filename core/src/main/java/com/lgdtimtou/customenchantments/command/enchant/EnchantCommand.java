package com.lgdtimtou.customenchantments.command.enchant;

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
        super("customenchant", new SubCommandAdd(), new SubCommandRemove());
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player player)){
            sender.sendMessage(Util.getMessage("OnlyPlayers"));
            return false;
        }
        if (args.length < 2){
            sender.sendMessage(Util.getMessage("EnchantCommandUsage"));
            return false;
        }

        EnchantSubCommand subCommand = (EnchantSubCommand) this.getSubCommand(args[0].toLowerCase());
        if (subCommand == null){
            sender.sendMessage(Util.getMessage("EnchantCommandUsage"));
            return false;
        }

        if (args[0].equalsIgnoreCase("getbook")){
            subCommand.execute(player, null, null, -1);
            return true;
        }

        String enchantName = args[1].toLowerCase();
        int level = 1;
        if (args.length > 2 && args[0].equalsIgnoreCase("add")){
            try {
                level = Integer.parseInt(args[2]);
            } catch (Exception e){
                player.sendMessage(Util.getMessage("EnchantCommandUsage"));
                return false;
            }
        }


        //Enchant bestaat niet
        CustomEnchant customEnchant;
        try {
            customEnchant = CustomEnchant.get(enchantName);
        } catch (IllegalArgumentException e) {
            player.sendMessage(Util.getMessage("NonExistingEnchant"));
            return false;
        }

        //Speler heeft niets vast
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR){
            player.sendMessage(Util.getMessage("EmptyHand"));
            return false;
        }

        ItemStack item = player.getInventory().getItemInMainHand();


        subCommand.execute(player, item, customEnchant, level);
        return true;
    }
}
