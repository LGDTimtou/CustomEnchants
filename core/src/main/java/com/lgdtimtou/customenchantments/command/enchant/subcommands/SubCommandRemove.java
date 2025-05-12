package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.command.Command;
import com.lgdtimtou.customenchantments.command.SubCommand;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubCommandRemove extends SubCommand {
    public SubCommandRemove(Command command) {
        super(command, "remove", 2, "EnchantSubCommandRemoveUsage");
    }

    @Override
    public List<String> getTabValues(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player))
            return null;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR || item.getItemMeta() == null)
            item = null;

        if (args.length == 2) {
            ItemStack finalItem = item;
            Stream<CustomEnchant> filtered = CustomEnchant.getCustomEnchantSet().stream()
                                                          .filter(ce -> ce.getName()
                                                                          .toLowerCase()
                                                                          .startsWith(args[1].toLowerCase()))
                                                          .filter(ce -> finalItem == null || finalItem.getItemMeta()
                                                                                                      .getEnchants()
                                                                                                      .containsKey(
                                                                                                              ce.getEnchantment()));
            return filtered.map(CustomEnchant::getNamespacedName).collect(Collectors.toList());
        } else return null;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Util.getMessage("OnlyPlayers"));
            return;
        }

        String enchantName = args[1].toLowerCase();

        //Enchant bestaat niet
        CustomEnchant customEnchant;
        try {
            customEnchant = CustomEnchant.get(enchantName);
        } catch (IllegalArgumentException e) {
            player.sendMessage(Util.getMessage("NonExistingEnchant"));
            return;
        }

        //Speler heeft niets vast
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage(Util.getMessage("EmptyHand"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (!item.containsEnchantment(customEnchant.getEnchantment())) {
            player.sendMessage(Util.getMessage("DoesntHaveEnchant").replace("%enchant%", customEnchant.getName()));
            return;
        }

        //Removing enchant
        item.removeEnchantment(customEnchant.getEnchantment());
    }
}
