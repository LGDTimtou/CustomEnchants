package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchantRecord;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.other.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SubCommandInfo extends EnchantSubCommand{

    public SubCommandInfo() {
        super("info", 0, "EnchantSubCommandInfoUsage");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 1) {
            TextComponent message = new TextComponent("§6§bLoaded Custom Enchantments:");
            for (CustomEnchant customEnchant : CustomEnchant.getCustomEnchantSet()) {
                String enchantName = customEnchant.getName();
                TextComponent enchantComponent = new TextComponent("\n§e- " + enchantName);

                enchantComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ce info " + enchantName.toLowerCase()));
                enchantComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aClick to see details for §b" + enchantName)));

                message.addExtra(enchantComponent);
            }

            player.spigot().sendMessage(message);
        } else {
            String enchantName = args[1];
            CustomEnchant customEnchant;
            try {
                customEnchant = CustomEnchant.get(enchantName);
            } catch (IllegalArgumentException e) {
                player.sendMessage(Util.getMessage("NonExistingEnchant"));
                return;
            }

            TextComponent title = new TextComponent("§6=== Enchantment: §b§l" + customEnchant.getName() + "§6 ===");
            TextComponent maxLevel = new TextComponent("\n§eMax Level: §f" + customEnchant.getMaxLevel());
            TextComponent targets = new TextComponent("\n§eTargets: §f" + customEnchant.getEnchantmentTargets().toString());

            TextComponent triggers = new TextComponent("\n§eTriggers: §f");
            for (EnchantTriggerType type : customEnchant.getTriggers().keySet()) {
                TextComponent trigger = new TextComponent("\n§7 - §b" + type.name());
                Set<String> conditions = customEnchant.getTriggers().get(type);
                if (conditions != null && !conditions.isEmpty()) {
                    trigger.addExtra("\n   §7Conditions: §f" + String.join(", ", conditions));
                }
                triggers.addExtra(trigger);
            }

            TextComponent message = new TextComponent();
            message.addExtra(title);
            message.addExtra(maxLevel);
            message.addExtra(targets);
            message.addExtra(triggers);

            player.spigot().sendMessage(message);
        }
    }

    @Override
    public List<String> getTabValues(CommandSender sender, String[] args) {
        if (args.length == 2)
            return CustomEnchant.getCustomEnchantSet().stream().map(CustomEnchant::getNamespacedName).collect(Collectors.toList());
        else return null;
    }
}
