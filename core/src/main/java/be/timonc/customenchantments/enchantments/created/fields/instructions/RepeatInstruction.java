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
    protected void execute(Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        if (amount == null || instructions == null) return;

        int max = parseDouble(player, amount, parameters).intValue();
        callIteration(0, max, player, customEnchant, parameters, executeNextInstruction);
    }

    private void callIteration(int i, int max, Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        String iteration = String.valueOf(i);
        if (i < max) {
            parameters.put(loopParameterName, () -> iteration);
            Instruction.executeInstructionQueue(
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
