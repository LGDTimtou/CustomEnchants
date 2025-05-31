package be.timonc.customenchantments.enchantments.created.fields.instructions;

import be.timonc.customenchantments.enchantments.created.fields.Instruction;
import be.timonc.customenchantments.enchantments.created.fields.InstructionCall;
import be.timonc.customenchantments.other.Util;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class RepeatInstruction extends Instruction {

    private String loopParameterName;
    private String amount;
    private Queue<Instruction> instructions;

    @Override
    protected void setValue(Object value) {
        try {
            Map<String, Object> castedValue = (Map<String, Object>) value;
            amount = String.valueOf(castedValue.getOrDefault("amount", 5));
            loopParameterName = String.valueOf(castedValue.getOrDefault("loop_parameter", "k"));
            instructions = Instruction.parseInstructions((List<?>) castedValue.get("instructions"));
        } catch (Exception e) {
            Util.error("Error while parsing 'repeat' instruction: " + value);
        }
    }

    @Override
    protected void execute(InstructionCall instructionCall, Runnable executeNextInstruction) {
        if (amount == null || instructions == null) return;

        int max = parseDouble(amount, instructionCall.getPlayer(), instructionCall.getParameters()).intValue();
        callIteration(0, max, instructionCall, executeNextInstruction);
    }

    private void callIteration(int i, int max, InstructionCall instructionCall, Runnable executeNextInstruction) {
        String iteration = String.valueOf(i);
        if (i < max) {
            instructionCall.getParameters().put(loopParameterName, () -> iteration);

            instructionCall.executeInstructionQueue(
                    new ArrayDeque<>(instructions),
                    () -> callIteration(
                            i + 1,
                            max,
                            instructionCall,
                            executeNextInstruction
                    )
            );
        } else {
            instructionCall.getParameters().remove(loopParameterName);
            executeNextInstruction.run();
        }
    }
}
