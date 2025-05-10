package com.lgdtimtou.customenchantments.command;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permissible;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Command implements CommandExecutor, TabCompleter {

    protected final String permission;
    private final String label;
    private final Map<String, SubCommand> subCommands;

    public Command(String label) {
        this.label = label;
        this.permission = "command." + label;
        this.subCommands = new HashMap<>();
    }

    protected boolean hasPermission(Permissible permissible) {
        return Util.hasPermission(permissible, permission);
    }

    protected Set<SubCommand> getSubCommands() {
        return new HashSet<>(subCommands.values());
    }

    protected void setSubCommands(SubCommand... subCommands) {
        Arrays.stream(subCommands).forEach(subCommand -> this.subCommands.put(subCommand.getLabel(), subCommand));
        PluginCommand command = Main.getMain().getCommand(label);
        assert command != null;
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    protected SubCommand getSubCommand(String key) {
        return subCommands.get(key);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 1)
            return getSubCommands().stream()
                                   .filter(subCommand -> subCommand.hasPermission(sender))
                                   .map(SubCommand::getLabel)
                                   .filter(v -> v.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        else if (args.length > 1 && this.getSubCommand(args[0].toLowerCase()) != null) {
            SubCommand subCommand = this.getSubCommand(args[0].toLowerCase());
            return subCommand.hasPermission(sender) ? subCommand.getTabValues(sender, args) : null;
        } else {
            return null;
        }
    }
}
