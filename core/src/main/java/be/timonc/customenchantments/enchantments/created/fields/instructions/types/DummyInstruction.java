package be.timonc.customenchantments.enchantments.created.fields.instructions.types;

import be.timonc.customenchantments.enchantments.created.fields.instructions.Instruction;
import be.timonc.customenchantments.enchantments.created.fields.instructions.InstructionCall;

public class DummyInstruction extends Instruction {

    @Override
    protected void setValue(Object value) {
    }

    @Override
    protected void execute(InstructionCall instructionCall, Runnable executeNextInstruction) {
        executeNextInstruction.run();
    }
}
