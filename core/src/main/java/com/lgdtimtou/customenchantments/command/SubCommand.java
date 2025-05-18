package com.lgdtimtou.customenchantments.command;

import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

import java.util.List;

public abstract class SubCommand {

    private final String permission;
    private final String label;
    private final int minArguments;
    private final String usageMessageID;

    public SubCommand(Command command, String label, int minArguments, String usageMessageID) {
        this.permission = command.permission;
        this.label = label;
        this.minArguments = minArguments + 1;
        this.usageMessageID = usageMessageID;
    }

    public String getLabel() {
        return label;
    }

    public int getMinArguments() {
        return minArguments;
    }

    public String getUsageMessage() {
        return usageMessageID;
    }

    public abstract List<String> getTabValues(CommandSender commandSender, String[] args);

    public abstract void execute(CommandSender commandSender, String[] args);

    public boolean hasPermission(Permissible permissible) {
        return Util.hasPermission(permissible, permission + "." + getLabel()) || Util.hasPermission(
                permissible,
                permission + ".*"
        );
    }
}
