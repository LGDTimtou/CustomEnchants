package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.command.Command;
import com.lgdtimtou.customenchantments.command.SubCommand;
import com.lgdtimtou.customenchantments.other.Files;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class SubCommandReload extends SubCommand {
    public SubCommandReload(Command command) {
        super(command, "reload", 0, "EnchantSubCommandReloadUsage");
    }

    @Override
    public List<String> getTabValues(CommandSender commandSender, String[] args) {
        return List.of();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Arrays.stream(Files.values()).forEach(Files::reloadConfig);
        commandSender.sendMessage(Util.getMessage("ConfigReloadSuccess"));
    }
}
