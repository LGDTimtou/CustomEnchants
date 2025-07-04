package be.timonc.customenchantments.enchantments.custom.fields.instructions.types;

import be.timonc.customenchantments.enchantments.custom.fields.instructions.Instruction;
import be.timonc.customenchantments.enchantments.custom.fields.instructions.InstructionCall;
import be.timonc.customenchantments.other.CustomEnchantLogFilter;
import be.timonc.customenchantments.other.Util;
import org.bukkit.Bukkit;

public class CommandInstruction extends Instruction {

    private String command;


    @Override
    protected void setValue(Object value) {
        this.command = String.valueOf(value);
    }

    @Override
    protected void execute(InstructionCall instructionCall, Runnable executeNextInstruction) {
        String executableCommand = parseNestedExpression(
                command,
                instructionCall.getPlayer(),
                instructionCall.getParameters()
        );
        Util.debug("Running command: " + executableCommand);

        CustomEnchantLogFilter.addFilter();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executableCommand);
        CustomEnchantLogFilter.removeFilter();

        executeNextInstruction.run();
    }
}
