package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SubCommandList extends EnchantSubCommand{

    public SubCommandList() {
        super("list", 0, "");
    }

    @Override
    public void execute(Player player, String[] args) {
        TextComponent message = new TextComponent("§6Custom Enchantments:\n");
        for (CustomEnchant customEnchant : CustomEnchant.getCustomEnchantSet()) {
            String enchantName = customEnchant.getName();
            TextComponent enchantComponent = new TextComponent("§e- " + enchantName + "\n");

            enchantComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ce list " + enchantName));
            enchantComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aClick to see details for §b" + enchantName)));

            message.addExtra(enchantComponent);
        }

        player.spigot().sendMessage(message);
    }

    @Override
    public List<String> getTabValues(CommandSender sender, String[] args) {
        if (!(sender instanceof Player))
            return null;

        return List.of();
    }
}
