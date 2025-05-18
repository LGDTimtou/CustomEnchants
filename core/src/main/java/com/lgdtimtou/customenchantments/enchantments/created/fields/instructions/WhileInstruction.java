package com.lgdtimtou.customenchantments.enchantments.created.fields.instructions;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantInstruction;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Supplier;

public class WhileInstruction extends CustomEnchantInstruction {

    private String conditionString;
    private String loopParameterName;
    private Queue<CustomEnchantInstruction> instructions;


    @Override
    protected void setValue(Object value) {
        try {
            Map<String, Object> castedValue = (Map<String, Object>) value;
            conditionString = String.valueOf(castedValue.get("condition"));
            loopParameterName = String.valueOf(castedValue.getOrDefault("loop_parameter", "k"));
            instructions = CustomEnchantInstruction.parseInstructions((List<?>) castedValue.get("instructions"));
        } catch (Exception e) {
            Util.error("Error while parsing 'while' instruction: " + value);
        }
    }


    @Override
    protected void execute(Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        callIteration(0, player, customEnchant, parameters, executeNextInstruction);
    }


    private void callIteration(int i, Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        boolean condition = parseCondition(player, conditionString, parameters);
        if (condition) {
            parameters.put(loopParameterName, () -> String.valueOf(i));
            CustomEnchantInstruction.executeInstructionQueue(
                    new ArrayDeque<>(instructions),
                    player,
                    customEnchant,
                    parameters,
                    () -> callIteration(i + 1, player, customEnchant, parameters, executeNextInstruction)
            );
        } else {
            parameters.remove(loopParameterName);
            executeNextInstruction.run();
        }
    }
}
