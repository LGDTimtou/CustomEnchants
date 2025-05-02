package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.other.Files;
import com.lgdtimtou.customenchantments.other.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class SubCommandEdit extends EnchantSubCommand {

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
        }

        String yaml = getYamlForEnchant(customEnchant);
        if (yaml == null) {
            player.sendMessage("&e" + customEnchant.getNamespacedName() + " was not found in enchantments.yml. This should not be possible! Report this bug");
            return;
        }
        System.out.println(yaml);

        String encoded = Base64.getUrlEncoder().encodeToString(yaml.getBytes(StandardCharsets.UTF_8));
        String url = "http://localhost:3000/custom_enchants/custom_enchant_builder?data=" + encoded;

        TextComponent message = new TextComponent("§eClick here to edit " + customEnchant.getNamespacedName());
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        player.spigot().sendMessage(message);
    }

    @Override
    public List<String> getTabValues(CommandSender sender, String[] args) {
        if (args.length == 2)
            return CustomEnchant.getCustomEnchantSet().stream()
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

        YamlConfiguration yaml = new YamlConfiguration();
        for (String key : section.getKeys(false))
            yaml.set(key, section.get(key));
        return yaml.saveToString();
    }
}
