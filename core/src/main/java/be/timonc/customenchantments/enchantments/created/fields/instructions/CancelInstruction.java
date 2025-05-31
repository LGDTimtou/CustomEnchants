package be.timonc.customenchantments.enchantments.created.fields.instructions;

import be.timonc.customenchantments.enchantments.created.fields.Instruction;
import be.timonc.customenchantments.enchantments.created.fields.InstructionCall;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerType;
import be.timonc.customenchantments.other.Util;

public class CancelInstruction extends Instruction {
    
    private TriggerType triggerType;

    @Override
    protected void setValue(Object value) {
        String triggerTypeString = (String) value;
        try {
            triggerType = TriggerType.valueOf(triggerTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            String closest = Util.findClosestMatch(triggerTypeString, TriggerType.class);
            String closest_message = closest == null ? "." : ", did you mean " + closest + "?";
            Util.warn(triggerTypeString.toUpperCase() + " is not a valid trigger" + closest_message + " Cancel instruction will cancel nothing...");
        }
    }

    @Override
    protected void execute(InstructionCall instructionCall, Runnable executeNextInstruction) {
        instructionCall.cancel(triggerType);
        executeNextInstruction.run();
    }
}
