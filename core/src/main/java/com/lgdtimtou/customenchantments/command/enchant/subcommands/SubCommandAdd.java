package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.other.Files;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
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



        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR || item.getItemMeta() == null)
            item = null;

        return switch(args.length){
            //Returns the names of all the custom enchants
            case 2 -> {
                ItemStack finalItem = item;
                Stream<CustomEnchant> filtered = CustomEnchant.getCustomEnchantSet().stream()
                        .filter(ce -> ce.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                        .filter(ce -> finalItem == null || !finalItem.getEnchantments().containsKey(ce.getEnchantment()))
                        .filter(ce -> finalItem == null || Files.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean()
                                || ce.getEnchantmentTargets().stream().anyMatch(target -> target.includes(finalItem)));
                yield filtered.map(CustomEnchant::getNamespacedName).collect(Collectors.toList());
            }
            // Checks if the given enchant exists
            // Return a list of all possible levels for that enchant otherwise -1
            case 3 -> {
                String enchant = args[1].toLowerCase();
                if (CustomEnchant.getCustomEnchantSet().stream().anyMatch(v -> v.getNamespacedName().equals(enchant))){
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
        if (item != null && item.containsEnchantment(enchantment.getEnchantment()) && level == item.getEnchantmentLevel(enchantment.getEnchantment())){
            player.sendMessage(Util.getMessage("AlreadyHasEnchant").replace("%enchant%", enchantment.getName())
                    .replace("%level%", CustomEnchant.getLevelRoman(level)));
            return;
        }


        if (level <= 0 || enchantment.getEnchantment().getMaxLevel() < level){
            player.sendMessage(Util.replaceParameters(Map.of(
                    "max_level", String.valueOf(enchantment.getEnchantment().getMaxLevel()),
                    "enchant", enchantment.getName()
            ), Util.getMessage("LevelRange")));
            return;
        }

        if (!enchantment.getEnchantment().canEnchantItem(item) && !Files.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean()){
            player.sendMessage(Util.getMessage("UnsafeEnchantment")
                    .replace("%enchant%", Util.title(enchantment.getName()))
                    .replace("%targets%", enchantment.getEnchantmentTargets().toString()));
            player.sendMessage(Util.getMessageNoPrefix("Setting")
                    .replace("%setting%", "allow_unsafe_enchantments"));
            return;
        }

        if (Files.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean())
            item.addUnsafeEnchantment(enchantment.getEnchantment(), level);
        else
            item.addEnchantment(enchantment.getEnchantment(), level);
    }
}
