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

public class RepeatInstruction extends CustomEnchantInstruction {

    private String loopParameterName;
    private String amount;
    private Queue<CustomEnchantInstruction> instructions;

    @Override
    protected void setValue(Object value) {
        try {
            Map<String, Object> castedValue = (Map<String, Object>) value;
            amount = String.valueOf(castedValue.get("amount"));
            loopParameterName = String.valueOf(castedValue.get("loop_parameter"));
            instructions = CustomEnchantInstruction.parseInstructions((List<?>) castedValue.get("instructions"));
        } catch (Exception e) {
            Util.error("Error while parsing 'repeat' instruction: " + value);
        }
    }

    @Override
    protected void execute(Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        if (amount == null || instructions == null) return;

        int max = parseValueAsDouble(amount, parameters).intValue();
        callIteration(0, max, player, customEnchant, parameters, executeNextInstruction);
    }

    private void callIteration(int i, int max, Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        String iteration = String.valueOf(i);
        if (i < max) {
            parameters.put(loopParameterName, () -> iteration);
            CustomEnchantInstruction.executeInstructionQueue(
                    new ArrayDeque<>(instructions),
                    player,
                    customEnchant,
                    parameters,
                    () -> callIteration(i + 1, max, player, customEnchant, parameters, executeNextInstruction)
            );
        } else {
            parameters.remove(loopParameterName);
            executeNextInstruction.run();
        }
    }
}
