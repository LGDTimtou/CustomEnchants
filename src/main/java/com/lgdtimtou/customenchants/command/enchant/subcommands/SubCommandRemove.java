package com.lgdtimtou.customenchants.command.enchant.subcommands;

import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import com.lgdtimtou.customenchants.Util;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubCommandRemove extends EnchantSubCommand{
    public SubCommandRemove() {
        super("remove");
    }

    @Override
    public List<String> getTabValues(CommandSender sender, String[] args) {
        if (!(sender instanceof Player))
            return null;

        Player player = (Player) sender;
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR)
            return null;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() == null)
            return null;

        if (args.length == 2){
            Stream<CustomEnchant> filtered = Arrays.stream(CustomEnchant.values())
                    .filter(ce -> ce.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                    .filter(ce -> item.getItemMeta().getEnchants().containsKey(ce.getEnchantment()));
            return filtered.map(ce -> ce.getName().toLowerCase()).collect(Collectors.toList());
        } else
            return null;
    }

    @Override
    public void execute(Player player, ItemStack item, CustomEnchant enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasEnchant(enchantment.getEnchantment())){
            player.sendMessage(Util.getMessage("DoesntHaveEnchant").replace("%enchant%", enchantment.getName()));
            return;
        }

        //Removing enchant
        meta.removeEnchant(enchantment.getEnchantment());

        //Removing lore
        List<String> lore = meta.getLore();
        if (lore == null)
            return;
        else {
            int index = -1;
            for (int i = 0; i < lore.size(); i++)
                if (lore.get(i).contains(enchantment.getLoreName()))
                    index = i;
            if (index != -1)
                lore.remove(index);
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
