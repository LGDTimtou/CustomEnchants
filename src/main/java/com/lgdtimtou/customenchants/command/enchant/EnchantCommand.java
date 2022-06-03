package com.lgdtimtou.customenchants.command.enchant;

import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import com.lgdtimtou.customenchants.Util;
import com.lgdtimtou.customenchants.command.Command;
import com.lgdtimtou.customenchants.command.enchant.subcommands.EnchantSubCommand;
import com.lgdtimtou.customenchants.command.enchant.subcommands.SubCommandAdd;
import com.lgdtimtou.customenchants.command.enchant.subcommands.SubCommandRemove;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class EnchantCommand extends Command {

    public EnchantCommand() {
        super("customenchant", new SubCommandAdd(), new SubCommandRemove());
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
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

        Player player = (Player) sender;
        String enchantName = args[1].toUpperCase();
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
        if (Arrays.stream(CustomEnchant.values()).noneMatch(v -> v.name().equals(enchantName))){
            player.sendMessage(Util.getMessage("NonExistingEnchant"));
            return false;
        }
        //Speler heeft niets vast
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR){
            player.sendMessage(Util.getMessage("EmptyHand"));
            return false;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        CustomEnchant enchantment = CustomEnchant.valueOf(enchantName);


        subCommand.execute(player, item, enchantment, level);
        return true;
    }
}
