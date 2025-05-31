package be.timonc.customenchantments.enchantments.created.fields.instructions;

import be.timonc.customenchantments.enchantments.created.fields.Instruction;
import be.timonc.customenchantments.enchantments.created.fields.InstructionCall;

public class DummyInstruction extends Instruction {

    @Override
    protected void setValue(Object value) {
    }

    @Override
    protected void execute(InstructionCall instructionCall, Runnable executeNextInstruction) {
        executeNextInstruction.run();
    }
}
