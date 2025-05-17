package com.lgdtimtou.customenchantments.enchantments.created.fields.instructions;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantInstruction;
import com.lgdtimtou.customenchantments.enchantments.created.fields.instructions.data.BooleanOperation;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Supplier;

public class ConditionalInstruction extends CustomEnchantInstruction {

    private String value1;
    private String value2;
    private BooleanOperation operation;
    private Queue<CustomEnchantInstruction> if_instructions;
    private Queue<CustomEnchantInstruction> else_instructions;

    @Override
    protected void setValue(Object value) {
        try {
            Map<String, Object> values = (Map<String, Object>) value;
            this.value1 = String.valueOf(values.get("value1")).toLowerCase();
            this.value2 = String.valueOf(values.get("value2")).toLowerCase();
            this.operation = BooleanOperation.valueOf(String.valueOf(values.get("operation")));

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
        boolean condition;
        String v1String = parseValue(player, value1, parameters);
        String v2String = parseValue(player, value2, parameters);
        try {
            Double v1 = Double.valueOf(v1String);
            Double v2 = Double.valueOf(v2String);
            condition = operation.apply(v1, v2);
        } catch (NumberFormatException ignored) {
            condition = operation.apply(v1String, v2String);
        }

        executeInstructionQueue(
                condition ? if_instructions : else_instructions,
                player,
                customEnchant,
                parameters,
                executeNextInstruction
        );
    }
}
