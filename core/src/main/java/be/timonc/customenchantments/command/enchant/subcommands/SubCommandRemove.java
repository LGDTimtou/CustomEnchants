package be.timonc.customenchantments.command.enchant.subcommands;

import be.timonc.customenchantments.command.Command;
import be.timonc.customenchantments.command.SubCommand;
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
import java.util.stream.Stream;

public class SubCommandRemove extends SubCommand {
    public SubCommandRemove(Command command) {
        super(command, "remove", 1, Message.COMMANDS__REMOVE__USAGE.get());
    }

    @Override
    public List<String> getTabValues(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player))
            return List.of();

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR || item.getItemMeta() == null)
            item = null;

        if (args.length == 2) {
            ItemStack finalItem = item;
            Stream<Enchantment> filtered = Registry.ENCHANTMENT.stream()
                                                               .filter(enchant -> enchant.getKey().getKey()
                                                                                         .startsWith(args[1].toLowerCase()))
                                                               .filter(enchant -> finalItem == null || finalItem.containsEnchantment(
                                                                       enchant));
            return filtered.map(enchant -> enchant.getKey().getKey()).collect(Collectors.toList());
        } else return List.of();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Message.COMMANDS__ONLY_PLAYERS.get());
            return;
        }

        String enchantName = args[1].toLowerCase();

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

        if (!item.containsEnchantment(enchantment)) {
            player.sendMessage(Message.COMMANDS__REMOVE__DOESNT_HAVE_ENCHANT.get(Map.of("enchant", enchantmentName)));
            return;
        }

        //Removing enchant
        item.removeEnchantment(enchantment);
    }
}
