package be.timonc.customenchantments.enchantments.custom.fields.instructions.types;

import be.timonc.customenchantments.enchantments.custom.fields.instructions.Instruction;
import be.timonc.customenchantments.enchantments.custom.fields.instructions.InstructionCall;
import be.timonc.customenchantments.other.Util;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ConditionalInstruction extends Instruction {

    private String condition;
    private Queue<Instruction> if_instructions;
    private Queue<Instruction> else_instructions;

    @Override
    protected void setValue(Object value) {
        try {
            Map<String, Object> values = (Map<String, Object>) value;
            condition = values.get("condition").toString();

            List<?> ifList = (List<?>) values.get("if");
            List<?> elseList = (List<?>) values.get("else");
            this.if_instructions = ifList != null ? Instruction.parseInstructions(ifList) : new ArrayDeque<>();
            this.else_instructions = elseList != null ? Instruction.parseInstructions(elseList) : new ArrayDeque<>();
        } catch (Exception e) {
            Util.error("Error while parsing 'conditional' instruction: " + value);
        }
    }


    @Override
    protected void execute(InstructionCall instructionCall, Runnable executeNextInstruction) {
        instructionCall.executeInstructionQueue(
                parseCondition(condition, instructionCall.getPlayer(), instructionCall.getParameters()) ?
                        new ArrayDeque<>(if_instructions) :
                        new ArrayDeque<>(else_instructions),
                executeNextInstruction
        );
    }
}
