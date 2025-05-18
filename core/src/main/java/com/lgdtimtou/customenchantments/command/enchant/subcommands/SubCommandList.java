package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.command.Command;
import com.lgdtimtou.customenchantments.command.SubCommand;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.other.Message;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SubCommandList extends SubCommand {

    public SubCommandList(Command command) {
        super(command, "list", 0, "EnchantSubCommandListUsage");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Message.COMMANDS__ONLY_PLAYERS.get());
            return;
        }
        // Default enchantments (not editable)
        TextComponent defaultHeader = new TextComponent("§6§lDefault Enchantments:\n");
        for (CustomEnchant customEnchant : CustomEnchant.getCustomEnchantSet()) {
            if (!customEnchant.isDefaultEnchantment()) continue;

            TextComponent defaultEnchant = new TextComponent("§7- " + customEnchant.getName() + "\n");
            defaultHeader.addExtra(defaultEnchant);
        }

        // Editable custom enchantments
        TextComponent customHeader = new TextComponent("\n§6§lCustom Enchantments:\n");
        for (CustomEnchant customEnchant : CustomEnchant.getCustomEnchantSet()) {
            if (customEnchant.isDefaultEnchantment()) continue;

            String enchantName = customEnchant.getNamespacedName();

            TextComponent enchantComponent = new TextComponent("§e- " + customEnchant.getName() + "\n");
            enchantComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ce edit " + enchantName));
            enchantComponent.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new Text("§7Click to edit §e" + customEnchant.getName())
            ));

            customHeader.addExtra(enchantComponent);
        }

        defaultHeader.addExtra(customHeader);

        player.spigot().sendMessage(defaultHeader);
    }


    @Override
    public List<String> getTabValues(CommandSender commandSender, String[] args) {
        return List.of();
    }
}
