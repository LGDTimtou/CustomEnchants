package be.timonc.customenchantments.command.enchant.subcommands;

import be.timonc.customenchantments.command.Command;
import be.timonc.customenchantments.command.SubCommand;
import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.other.File;
import be.timonc.customenchantments.other.Message;
import be.timonc.customenchantments.other.Util;
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
        super(command, "add", 1, Message.COMMANDS__ADD__USAGE.get());
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
                                                                   .filter(enchant -> finalItem == null || File.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean()
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
            commandSender.sendMessage(Message.COMMANDS__ONLY_PLAYERS.get());
            return;
        }
        String enchantName = args[1].toLowerCase();
        int level = 1;
        if (args.length > 2) {
            try {
                level = Integer.parseInt(args[2]);
            } catch (Exception e) {
                player.sendMessage(Message.COMMANDS__ADD__USAGE.get());
                return;
            }
        }

        //Enchant bestaat niet
        Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchantName));
        if (enchantment == null) {
            player.sendMessage(Message.COMMANDS__NON_EXISTING_ENCHANT.get());
            return;
        }
        String enchantmentName = Util.title(enchantment.getKey().getKey());

        //Speler heeft niets vast
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage(Message.COMMANDS__EMPTY_HAND.get());
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.containsEnchantment(enchantment) && level == item.getEnchantmentLevel(enchantment)) {
            player.sendMessage(Message.COMMANDS__ADD__ALREADY_HAS_ENCHANT.get(Map.of(
                    "enchant",
                    enchantmentName,
                    "level",
                    CustomEnchant.getLevelRoman(level)
            )));
            return;
        }

        if (level <= 0 || enchantment.getMaxLevel() < level) {
            player.sendMessage(Util.replaceParameters(
                    player,
                    Message.COMMANDS__ADD__LEVEL_RANGE.get(),
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

        boolean unsafeEnchantmentsAllowed = File.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean();

        // Conflicting enchantments
        List<String> conflictingEnchantments = item.getEnchantments().keySet().stream()
                                                   .filter(ench -> ench.conflictsWith(enchantment))
                                                   .map(ench -> ench.getKey().toString())
                                                   .toList();
        if (!conflictingEnchantments.isEmpty() && !unsafeEnchantmentsAllowed) {
            player.sendMessage(Message.COMMANDS__ADD__CONFLICTING_ENCHANTMENT.get(Map.of(
                    "enchant",
                    enchantmentName,
                    "conflicting_enchantments",
                    conflictingEnchantments.toString()
            )));
            player.sendMessage(Message.GLOBAL__SETTING.getNoPrefix(Map.of("setting", "allow_unsafe_enchantments")));
            return;
        }

        // Correct tool
        boolean correctTool = enchantment.canEnchantItem(item);
        if (!correctTool && !unsafeEnchantmentsAllowed) {
            player.sendMessage(Message.COMMANDS__ADD__UNSAFE_ENCHANTMENT.get(Map.of(
                    "enchant", enchantmentName,
                    "item", player.getInventory().getItemInMainHand().getType().toString()
            )));
            player.sendMessage(Message.GLOBAL__SETTING.getNoPrefix(Map.of("setting", "allow_unsafe_enchantments")));
            return;
        }

        if (File.ConfigValue.ALLOW_UNSAFE_ENCHANTMENTS.getBoolean())
            item.addUnsafeEnchantment(enchantment, level);
        else
            item.addEnchantment(enchantment, level);
    }
}
