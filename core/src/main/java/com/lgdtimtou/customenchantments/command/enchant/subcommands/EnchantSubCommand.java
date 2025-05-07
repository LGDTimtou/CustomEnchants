package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.command.SubCommand;
import org.bukkit.command.CommandSender;

public abstract class EnchantSubCommand implements SubCommand {

    private final String label;
    private final int minArguments;
    private final String usageMessageID;

    public EnchantSubCommand(String label, int minArguments, String usageMessageID) {
        this.label = label;
        this.minArguments = minArguments;
        this.usageMessageID = usageMessageID;
    }

    public String getLabel() {
        return label;
    }

    public int getMinArguments() {
        return minArguments;
    }

    public String getUsageMessageID() {
        return usageMessageID;
    }

    public abstract void execute(CommandSender commandSender, String[] args);
}
