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
        super("add", 1, "EnchantSubCommandAddUsage");
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
    public void execute(Player player, String[] args) {
        String enchantName = args[1].toLowerCase();
        int level = 1;
        if (args.length > 2){
            try {
                level = Integer.parseInt(args[2]);
            } catch (Exception e){
                player.sendMessage(Util.getMessage("EnchantCommandUsage"));
                return;
            }
        }

        //Enchant bestaat niet
        CustomEnchant customEnchant;
        try {
            customEnchant = CustomEnchant.get(enchantName);
        } catch (IllegalArgumentException e) {
            player.sendMessage(Util.getMessage("NonExistingEnchant"));
            return;
        }

        //Speler heeft niets vast
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR){
            player.sendMessage(Util.getMessage("EmptyHand"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();


        if (item.containsEnchantment(customEnchant.getEnchantment()) && level == item.getEnchantmentLevel(customEnchant.getEnchantment())){
            player.sendMessage(Util.getMessage("AlreadyHasEnchant").replace("%enchant%", customEnchant.getName())
                    .replace("%level%", CustomEnchant.getLevelRoman(level)));
            return;
        }


        if (level <= 0 || customEnchant.getEnchantment().getMaxLevel() < level){
            player.sendMessage(Util.replaceParameters(Map.of(
                    "max_level", String.valueOf(customEnchant.getEnchantment().getMaxLevel()),
                    "enchant", customEnchant.getName()
            ), Util.getMessage("LevelRange")));
            return;
        }

        if (!customEnchant.getEnchantment().canEnchantItem(item) && !Files.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean()){
            player.sendMessage(Util.getMessage("UnsafeEnchantment")
                    .replace("%enchant%", Util.title(customEnchant.getName()))
                    .replace("%targets%", customEnchant.getEnchantmentTargets().toString()));
            player.sendMessage(Util.getMessageNoPrefix("Setting")
                    .replace("%setting%", "allow_unsafe_enchantments"));
            return;
        }

        if (Files.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean())
            item.addUnsafeEnchantment(customEnchant.getEnchantment(), level);
        else
            item.addEnchantment(customEnchant.getEnchantment(), level);
    }

}
