package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.command.Command;
import com.lgdtimtou.customenchantments.command.SubCommand;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.other.Message;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class SubCommandEdit extends SubCommand {

    public SubCommandEdit(Command command) {
        super(command, "edit", 1, Message.COMMANDS__EDIT__USAGE.get());
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        String enchantmentName = args[1].toLowerCase();

        //Enchant bestaat niet
        CustomEnchant customEnchant;
        try {
            customEnchant = CustomEnchant.get(enchantmentName);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(Message.COMMANDS__NON_EXISTING_ENCHANT.get());
            return;
        }

        // Default enchantments
        if (customEnchant.isDefaultEnchantment()) {
            commandSender.sendMessage(Message.COMMANDS__EDIT__DEFAULT_ENCHANT.get());
            return;
        }

        Main.getWebSocketConnection().sendEnchantment(commandSender, customEnchant);
    }

    @Override
    public List<String> getTabValues(CommandSender commandSender, String[] args) {
        if (args.length == 2)
            return CustomEnchant.getCustomEnchantSet().stream()
                                .filter(ce -> !ce.isDefaultEnchantment())
                                .filter(ce -> ce.getName()
                                                .toLowerCase()
                                                .startsWith(args[1].toLowerCase()))
                                .map(CustomEnchant::getNamespacedName)
                                .collect(Collectors.toList());
        else return List.of();
    }
}
