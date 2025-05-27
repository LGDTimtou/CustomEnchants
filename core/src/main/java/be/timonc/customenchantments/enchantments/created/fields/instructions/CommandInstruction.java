package be.timonc.customenchantments.enchantments.created.fields.instructions;

import be.timonc.customenchantments.enchantments.created.fields.Instruction;
import be.timonc.customenchantments.other.CustomEnchantLogFilter;
import be.timonc.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Supplier;

public class CommandInstruction extends Instruction {

    private String command;


    @Override
    protected void setValue(Object value) {
        this.command = String.valueOf(value);
    }

    @Override
    protected void execute(Player player, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        String executableCommand = parseNestedExpression(command, player, parameters);
        Util.debug("Running command: " + executableCommand);

        CustomEnchantLogFilter.addFilter();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executableCommand);

        CustomEnchantLogFilter.removeFilter();

        executeNextInstruction.run();
    }
}
