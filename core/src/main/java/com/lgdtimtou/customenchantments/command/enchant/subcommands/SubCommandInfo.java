package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SubCommandInfo extends EnchantSubCommand {

    public SubCommandInfo() {
        super("info", 1, "EnchantSubCommandInfoUsage");
    }

    @Override
    public void execute(Player player, String[] args) {
        TextComponent message = new TextComponent("§6§bLoaded Custom Enchantments:");
        for (CustomEnchant customEnchant : CustomEnchant.getCustomEnchantSet()) {
            String enchantName = customEnchant.getName();
            TextComponent enchantComponent = new TextComponent("\n§e- " + enchantName);
            message.addExtra(enchantComponent);
        }

        player.spigot().sendMessage(message);
    }

    @Override
    public List<String> getTabValues(CommandSender sender, String[] args) {
        return null;
    }
}
