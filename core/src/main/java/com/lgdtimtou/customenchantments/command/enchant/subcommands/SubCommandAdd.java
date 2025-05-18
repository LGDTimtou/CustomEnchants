package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.command.Command;
import com.lgdtimtou.customenchantments.command.SubCommand;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.other.Files;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SubCommandAdd extends SubCommand {

    public SubCommandAdd(Command command) {
        super(command, "add", 1, "EnchantSubCommandAddUsage");
    }

    @Override
    public List<String> getTabValues(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player))
            return List.of();

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR || item.getItemMeta() == null)
            item = null;

        return switch (args.length) {
            //Returns the names of all the custom enchants
            case 2 -> {
                ItemStack finalItem = item;
                Stream<Enchantment> filtered = Registry.ENCHANTMENT.stream()
                                                                   .filter(enchant -> enchant.getKey().getKey()
                                                                                             .startsWith(args[1].toLowerCase()))
                                                                   .filter(enchant -> finalItem == null || !finalItem.containsEnchantment(
                                                                           enchant))
                                                                   .filter(enchant -> finalItem == null || Files.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean()
                                                                           || enchant.canEnchantItem(finalItem));
                yield filtered.map(enchant -> enchant.getKey().getKey()).collect(Collectors.toList());
            }
            // Checks if the given enchant exists
            // Return a list of all possible levels for that enchant otherwise -1
            case 3 -> {
                String enchantName = args[1].toLowerCase();
                Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchantName));
                if (enchantment != null) {
                    yield IntStream.range(1, enchantment.getMaxLevel() + 1)
                                   .mapToObj(Integer::toString)
                                   .collect(Collectors.toList());
                } else {
                    yield List.of();
                }
            }
            default -> List.of();
        };
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Util.getMessage("OnlyPlayers"));
            return;
        }
        String enchantName = args[1].toLowerCase();
        int level = 1;
        if (args.length > 2) {
            try {
                level = Integer.parseInt(args[2]);
            } catch (Exception e) {
                player.sendMessage(Util.getMessage("EnchantCommandUsage"));
                return;
            }
        }

        //Enchant bestaat niet
        Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchantName));
        if (enchantment == null) {
            player.sendMessage(Util.getMessage("NonExistingEnchant"));
            return;
        }
        String enchantmentName = Util.title(enchantment.getKey().getKey());

        //Speler heeft niets vast
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage(Util.getMessage("EmptyHand"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.containsEnchantment(enchantment) && level == item.getEnchantmentLevel(enchantment)) {
            player.sendMessage(Util.getMessage("AlreadyHasEnchant").replace("%enchant%", enchantmentName)
                                   .replace("%level%", CustomEnchant.getLevelRoman(level)));
            return;
        }

        if (level <= 0 || enchantment.getMaxLevel() < level) {
            player.sendMessage(Util.replaceParameters(
                    player,
                    Util.getMessage("LevelRange"),
                    Map.of(
                            "max_level", () -> String.valueOf(enchantment.getMaxLevel()),
                            "enchant", () -> enchantName
                    )
            ));
            return;
        }

        // Same enchantment
        if (item.containsEnchantment(enchantment)) {
            item.removeEnchantment(enchantment);
            item.addUnsafeEnchantment(enchantment, level);
            return;
        }

        boolean unsafeEnchantmentsAllowed = Files.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean();

        // Conflicting enchantments
        List<String> conflictingEnchantments = item.getEnchantments().keySet().stream()
                                                   .filter(ench -> ench.conflictsWith(enchantment))
                                                   .map(ench -> ench.getKey().toString())
                                                   .toList();
        if (!conflictingEnchantments.isEmpty() && !unsafeEnchantmentsAllowed) {
            player.sendMessage(Util.getMessage("ConflictingEnchantment")
                                   .replace("%enchantment%", enchantmentName)
                                   .replace("%conflicting_enchantments%", conflictingEnchantments.toString())
            );
            player.sendMessage(Util.getMessageNoPrefix("Setting")
                                   .replace("%setting%", "allow_unsafe_enchantments"));
            return;
        }

        // Correct tool
        boolean correctTool = enchantment.canEnchantItem(item);
        if (!correctTool && !unsafeEnchantmentsAllowed) {
            player.sendMessage(Util.getMessage("UnsafeEnchantment")
                                   .replace("%enchant%", enchantmentName)
                                   .replace(
                                           "%item%",
                                           player.getInventory().getItemInMainHand().getType().toString()
                                   ));
            player.sendMessage(Util.getMessageNoPrefix("Setting")
                                   .replace("%setting%", "allow_unsafe_enchantments"));
            return;
        }

        if (Files.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean())
            item.addUnsafeEnchantment(enchantment, level);
        else
            item.addEnchantment(enchantment, level);
    }
}
