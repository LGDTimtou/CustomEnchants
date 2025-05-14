package com.lgdtimtou.customenchantments.enchantments.created.fields;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.fields.instructions.*;
import com.lgdtimtou.customenchantments.other.FileFunction;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Supplier;

public abstract class CustomEnchantInstruction {

    private static final Map<String, Class<? extends CustomEnchantInstruction>> customEnchantInstructions = Map.of(
            "command", CommandInstruction.class,
            "delay", DelayInstruction.class,
            "repeat", RepeatInstruction.class,
            "save", SaveInstruction.class,
            "load", LoadInstruction.class
    );

    public static CustomEnchantInstruction getInstruction(String name, Object value) {
        // Check if the instruction type exists in the map
        if (!customEnchantInstructions.containsKey(name)) {
            Util.warn("Instruction type '" + name + "' does not exist");
            return new DummyInstruction();
        }

        // Retrieve the instruction class
        Class<? extends CustomEnchantInstruction> instructionClass = customEnchantInstructions.get(name);

        try {
            // Create an instance of the instruction class
            CustomEnchantInstruction instruction = instructionClass.getDeclaredConstructor().newInstance();
            instruction.setValue(value);
            return instruction;
        } catch (Exception e) {
            Util.warn("Failed to create instruction: " + name + ". Error: " + e.getMessage());
            return new DummyInstruction();
        }
    }

    public static Queue<CustomEnchantInstruction> parseInstructions(List<?> unparsedInstructions) {
        Queue<CustomEnchantInstruction> instructionQueue = new ArrayDeque<>();

        for (Object unparsedInstruction : unparsedInstructions) {
            if (unparsedInstruction instanceof Map<?, ?> instructionMap) {

                for (Map.Entry<?, ?> entry : instructionMap.entrySet()) {
                    String instructionType = entry.getKey().toString();
                    Object value = entry.getValue();
                    instructionQueue.add(getInstruction(instructionType, value));
                }
            }
        }
        return instructionQueue;
    }

    public static void executeInstructionQueue(Queue<CustomEnchantInstruction> instructionQueue, Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable oncomplete) {
        CustomEnchantInstruction instruction = instructionQueue.poll();
        if (instruction == null) {
            oncomplete.run();
            return;
        }
        instruction.execute(
                player,
                customEnchant,
                parameters,
                () -> executeInstructionQueue(instructionQueue, player, customEnchant, parameters, oncomplete)
        );
    }

    protected abstract void setValue(Object value);

    protected void execute(Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        execute(player, parameters, executeNextInstruction);
    }

    protected void execute(Player player, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
    }

    protected String parseValue(Player player, String value, Map<String, Supplier<String>> parameters) {
        //Replace parameters
        String newValue = Util.replaceParameters(player, value, parameters);

        //Execute file functions
        try {
            newValue = FileFunction.parse(newValue);
        } catch (NumberFormatException exception) {
            Util.error("Error when trying to parse the functions of the following value: " + newValue);
        }
        return newValue;
    }

    protected Double parseValueAsDouble(Player player, String value, Map<String, Supplier<String>> parameters) {
        String stringValue = parseValue(player, value, parameters);
        try {
            return Double.parseDouble(stringValue);
        } catch (NumberFormatException exception) {
            Util.warn("Couldn't parse value to number: " + stringValue + ". Defaulting to 0");
            return 0.0;
        }
    }
}


