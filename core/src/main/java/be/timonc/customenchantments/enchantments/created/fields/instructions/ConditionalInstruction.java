package be.timonc.customenchantments.enchantments.created.fields.instructions;

import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.enchantments.created.fields.Instruction;
import be.timonc.customenchantments.other.Util;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Supplier;

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
    protected void execute(Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        executeInstructionQueue(
                parseCondition(player, condition, parameters) ?
                        new ArrayDeque<>(if_instructions) :
                        new ArrayDeque<>(else_instructions),
                player,
                customEnchant,
                parameters,
                executeNextInstruction
        );
    }
}
