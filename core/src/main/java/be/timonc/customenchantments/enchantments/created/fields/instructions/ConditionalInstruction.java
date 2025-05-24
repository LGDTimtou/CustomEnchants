package be.timonc.customenchantments.enchantments.created.fields.instructions;

import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.enchantments.created.fields.CustomEnchantInstruction;
import be.timonc.customenchantments.other.Util;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Supplier;

public class ConditionalInstruction extends CustomEnchantInstruction {

    private String condition;
    private Queue<CustomEnchantInstruction> if_instructions;
    private Queue<CustomEnchantInstruction> else_instructions;

    @Override
    protected void setValue(Object value) {
        try {
            Map<String, Object> values = (Map<String, Object>) value;
            condition = values.get("condition").toString();

            List<?> ifList = (List<?>) values.get("if");
            List<?> elseList = (List<?>) values.get("else");
            this.if_instructions = ifList != null ? CustomEnchantInstruction.parseInstructions(ifList) : new ArrayDeque<>();
            this.else_instructions = elseList != null ? CustomEnchantInstruction.parseInstructions(elseList) : new ArrayDeque<>();
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
