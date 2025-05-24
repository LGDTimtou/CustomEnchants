package be.timonc.customenchantments.command.enchant.subcommands;

import be.timonc.customenchantments.command.Command;
import be.timonc.customenchantments.command.SubCommand;
import be.timonc.customenchantments.other.File;
import be.timonc.customenchantments.other.Message;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class SubCommandReload extends SubCommand {
    public SubCommandReload(Command command) {
        super(command, "reload", 0, Message.COMMANDS__RELOAD__USAGE.get());
    }

    @Override
    public List<String> getTabValues(CommandSender commandSender, String[] args) {
        return List.of();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Arrays.stream(File.values()).forEach(File::reloadConfig);
        commandSender.sendMessage(Message.COMMANDS__RELOAD__SUCCESS.get());
    }
}
