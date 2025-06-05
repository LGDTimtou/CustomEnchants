package be.timonc.customenchantments.enchantments.custom.fields.instructions.types;

import be.timonc.customenchantments.enchantments.custom.fields.instructions.Instruction;
import be.timonc.customenchantments.enchantments.custom.fields.instructions.InstructionCall;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerType;
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
            String closest_message = closest == null ? "" : " Did you mean " + closest + "?";
            Util.error("Cannot cancel trigger: " + triggerTypeString.toUpperCase() + ". It is not a valid trigger type." + closest_message);
        }
    }

    @Override
    protected void execute(InstructionCall instructionCall, Runnable executeNextInstruction) {
        if (triggerType != null) instructionCall.cancel(triggerType);
        executeNextInstruction.run();
    }
}
