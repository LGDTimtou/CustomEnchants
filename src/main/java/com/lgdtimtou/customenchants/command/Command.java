package com.lgdtimtou.customenchants.command;

import com.lgdtimtou.customenchants.Main;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Command implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommands;

    public Command(String label, SubCommand... subCommands){
        this.subCommands = new HashMap<>();
        Arrays.stream(subCommands).forEach(subCommand -> this.subCommands.put(subCommand.getLabel(), subCommand));
        PluginCommand command = Main.getMain().getCommand(label);
        assert command != null;
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    protected Set<SubCommand> getSubCommands(){
        return new HashSet<>(subCommands.values());
    }

    protected SubCommand getSubCommand(String key){
        return subCommands.get(key);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 1)
            return getSubCommands().stream().map(SubCommand::getLabel)
                .filter(v -> v.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        else if (args.length > 1 && this.getSubCommand(args[0].toLowerCase()) != null){
            return this.getSubCommand(args[0].toLowerCase()).getTabValues(sender, args);
        } else{
            return null;
        }
    }
}
