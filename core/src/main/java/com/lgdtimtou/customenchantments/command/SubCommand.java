package com.lgdtimtou.customenchantments.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {

    List<String> getTabValues(CommandSender sender, String[] args);

    String getLabel();

}
