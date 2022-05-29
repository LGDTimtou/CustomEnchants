package com.lgdtimtou.replenish.command;

import com.lgdtimtou.replenish.CustomEnchant;
import com.lgdtimtou.replenish.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;


public class EnchantCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        if (args.length < 1){
            player.sendMessage(Util.getMessage("EnchantCommandUsage"));
            return false;
        }

        String enchantName = args[0].toUpperCase();
        int level = 1;
        if (args.length > 1){
            try {
                level = Integer.parseInt(args[1]);
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
        ItemMeta meta = item.getItemMeta();
        CustomEnchant enchantment = CustomEnchant.valueOf(enchantName);

        if (meta != null && meta.hasEnchant(enchantment.getEnchantment())){
            player.sendMessage(Util.getMessage("AlreadyHasEnchant").replace("%enchant%", enchantment.getName()));
            return false;
        }
        if (level < 0) {
            player.sendMessage(Util.getMessage("LevelNegative"));
            return false;
        }
        if (enchantment.getEnchantment().getMaxLevel() < level){
            player.sendMessage(Util.getMessage("MaxLevelExceeded").replace("%level%", String.valueOf(enchantment.getEnchantment().getMaxLevel()))
                    .replace("%enchant%", enchantment.getName()));
            return false;
        }


        //Adding enchantment lore
        String enchantLore = ChatColor.GRAY + enchantment.getName() + " " + CustomEnchant.getLevelRoman(level);
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = List.of(enchantLore);
        else
            lore.add(0, enchantLore);
        meta.setLore(lore);


        item.setItemMeta(meta);
        item.addUnsafeEnchantment(enchantment.getEnchantment(), level);
        return true;
    }
}
