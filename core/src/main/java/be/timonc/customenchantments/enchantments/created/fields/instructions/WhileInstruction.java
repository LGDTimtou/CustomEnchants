package be.timonc.customenchantments.enchantments.created.fields.instructions;

import be.timonc.customenchantments.enchantments.created.fields.Instruction;
import be.timonc.customenchantments.enchantments.created.fields.InstructionCall;
import be.timonc.customenchantments.other.Util;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Supplier;

public class WhileInstruction extends Instruction {

    private String conditionString;
    private String loopParameterName;
    private Queue<Instruction> instructions;


    @Override
    protected void setValue(Object value) {
        try {
            Map<String, Object> castedValue = (Map<String, Object>) value;
            conditionString = String.valueOf(castedValue.get("condition"));
            loopParameterName = String.valueOf(castedValue.getOrDefault("loop_parameter", "k"));
            instructions = Instruction.parseInstructions((List<?>) castedValue.get("instructions"));
        } catch (Exception e) {
            Util.error("Error while parsing 'while' instruction: " + value);
        }
    }


    @Override
    protected void execute(InstructionCall instructionCall, Runnable executeNextInstruction) {
        callIteration(0, instructionCall, executeNextInstruction);
    }


    private void callIteration(int i, InstructionCall instructionCall, Runnable executeNextInstruction) {
        Player player = instructionCall.getPlayer();
        Map<String, Supplier<String>> parameters = instructionCall.getParameters();
        boolean condition = parseCondition(conditionString, player, parameters);
        if (condition) {
            parameters.put(loopParameterName, () -> String.valueOf(i));
            instructionCall.executeInstructionQueue(
                    new ArrayDeque<>(instructions),
                    () -> callIteration(i + 1, instructionCall, executeNextInstruction)
            );
        } else {
            parameters.remove(loopParameterName);
            executeNextInstruction.run();
        }
    }
}
