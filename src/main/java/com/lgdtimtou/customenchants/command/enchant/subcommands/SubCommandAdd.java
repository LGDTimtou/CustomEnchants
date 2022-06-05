package com.lgdtimtou.customenchants.command.enchant.subcommands;

import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import com.lgdtimtou.customenchants.other.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SubCommandAdd extends EnchantSubCommand {

    public SubCommandAdd() {
        super("add");
    }

    @Override
    public List<String> getTabValues(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player))
            return null;

        if (player.getInventory().getItemInMainHand().getType() == Material.AIR)
            return null;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() == null)
            return null;

        return switch(args.length){
            //Returns the names of all the custom enchants
            case 2 -> {
                Stream<CustomEnchant> filtered = CustomEnchant.getCustomEnchantSet().stream()
                        .filter(ce -> ce.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                        .filter(ce -> !item.getEnchantments().containsKey(ce.getEnchantment()));
                yield filtered.map(ce -> ce.getName().toLowerCase()).collect(Collectors.toList());
            }
            //Checks if the given enchant at place 1 exists if so
            // it'll return a list of all possible levels for that enchant else -1
            case 3 -> {
                String enchant = args[1].toLowerCase();
                if (CustomEnchant.getCustomEnchantSet().stream().anyMatch(v -> v.getName().equals(enchant))){
                    int maxLvl = CustomEnchant.get(enchant).getEnchantment().getMaxLevel();
                    yield IntStream.range(1, maxLvl + 1).mapToObj(Integer::toString).collect(Collectors.toList());
                } else {
                    yield List.of("-1");
                }
            }
            default -> null;
        };
    }

    @Override
    public void execute(Player player, ItemStack item, CustomEnchant enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasEnchant(enchantment.getEnchantment()) && level == meta.getEnchantLevel(enchantment.getEnchantment())){
            player.sendMessage(Util.getMessage("AlreadyHasEnchant").replace("%enchant%", enchantment.getName())
                    .replace("%level%", CustomEnchant.getLevelRoman(level)));
            return;
        }
        if (level < 0) {
            player.sendMessage(Util.getMessage("LevelNegative"));
            return;
        }
        if (enchantment.getEnchantment().getMaxLevel() < level){
            player.sendMessage(Util.getMessage("MaxLevelExceeded").replace("%level%", String.valueOf(enchantment.getEnchantment().getMaxLevel()))
                    .replace("%enchant%", enchantment.getName()));
            return;
        }


        //Adding or replacing enchantment lore
        String enchantLore = ChatColor.GRAY + enchantment.getLoreName() + " " + CustomEnchant.getLevelRoman(level);
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = List.of(enchantLore);
        else {
            boolean replaced = false;
            for (int i = 0; i < lore.size(); i++) {
                if (lore.get(i).contains(enchantment.getLoreName())){
                    lore.set(i, enchantLore);
                    replaced = true;
                }
            }
            if (!replaced)
                lore.add(0, enchantLore);
        }
        meta.setLore(lore);


        item.setItemMeta(meta);
        item.addUnsafeEnchantment(enchantment.getEnchantment(), level);
    }
}
