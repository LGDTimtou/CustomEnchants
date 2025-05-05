package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.other.Files;
import com.lgdtimtou.customenchantments.other.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class SubCommandEdit extends EnchantSubCommand {

    private final String TIMONC_URL = "https://timonc.be/custom_enchants/custom_enchant_builder?load_yaml=true";

    public SubCommandEdit() {
        super("edit", 2, "EnchantSubCommandEditUsage");
    }

    @Override
    public void execute(Player player, String[] args) {
        String enchantmentName = args[1].toLowerCase();

        //Enchant bestaat niet
        CustomEnchant customEnchant;
        try {
            customEnchant = CustomEnchant.get(enchantmentName);
        } catch (IllegalArgumentException e) {
            player.sendMessage(Util.getMessage("NonExistingEnchant"));
            return;
        }

        // Default enchantments
        if (customEnchant.isDefaultEnchantment()) {
            player.sendMessage(Util.getMessage("EditDefaultEnchant"));
            return;
        }

        String yaml = getYamlForEnchant(customEnchant);
        if (yaml == null) {
            player.sendMessage("§c" + customEnchant.getNamespacedName() + " was not found in §eenchantments.yml.§c This should not be possible! Report this bug");
            return;
        }

        // Click to copy YAML
        TextComponent copyYaml = new TextComponent("1. §6§l[Click here] ");
        copyYaml.setBold(true);
        copyYaml.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, yaml));
        copyYaml.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new Text("§fCopy this enchantment's §eYAML definition §fto your clipboard")
        ));
        copyYaml.addExtra(new TextComponent("§7to copy §e" + customEnchant.getName() + "§7 YAML"));

        // Open editor
        TextComponent openEditor = new TextComponent("2. §b§l[Click Here] ");
        openEditor.setBold(true);
        openEditor.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, TIMONC_URL));
        openEditor.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new Text("§fOpen the interactive §bCustom Enchant Editor §fin your browser")
        ));
        openEditor.addExtra(new TextComponent("§7to edit §b" + customEnchant.getName()));

        // Header
        TextComponent header = new TextComponent("§a§m------------------------------------------------\n");
        TextComponent footer = new TextComponent("\n§a§m------------------------------------------------");

        TextComponent pasteInstruction = new TextComponent(
                "§l3. §7Paste the copied YAML into §eenchantments.yml§7");
        TextComponent restartServer = new TextComponent("§l4.1. §7Restart the server to apply your changes!");
        TextComponent reloadServer = new TextComponent(
                "§l4.2. §7Reloading is for testing non-vanilla changes only!");

        player.spigot().sendMessage(header);
        player.spigot().sendMessage(copyYaml);
        player.spigot().sendMessage(openEditor);
        player.spigot().sendMessage(pasteInstruction);
        player.spigot().sendMessage(restartServer);
        player.spigot().sendMessage(reloadServer);
        player.spigot().sendMessage(footer);
    }

    @Override
    public List<String> getTabValues(CommandSender sender, String[] args) {
        if (args.length == 2)
            return CustomEnchant.getCustomEnchantSet().stream()
                                .filter(ce -> !ce.isDefaultEnchantment())
                                .filter(ce -> ce.getName()
                                                .toLowerCase()
                                                .startsWith(args[1].toLowerCase()))
                                .map(CustomEnchant::getNamespacedName)
                                .collect(Collectors.toList());
        else return null;
    }

    private String getYamlForEnchant(CustomEnchant customEnchant) {
        String enchantmentName = customEnchant.getNamespacedName();
        ConfigurationSection section = Files.ENCHANTMENTS.getConfig().getConfigurationSection(enchantmentName);
        if (section == null) return null;

        YamlConfiguration fullYaml = new YamlConfiguration();
        YamlConfiguration inner = new YamlConfiguration();

        for (String key : section.getKeys(false)) {
            inner.set(key, section.get(key));
        }

        fullYaml.set(enchantmentName, inner);
        return fullYaml.saveToString();
    }
}
