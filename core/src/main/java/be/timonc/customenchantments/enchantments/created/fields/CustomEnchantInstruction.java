package be.timonc.customenchantments.enchantments.created.fields;

import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.enchantments.created.fields.instructions.*;
import be.timonc.customenchantments.other.Util;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ParseException;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CustomEnchantInstruction {

    private static final Map<String, Class<? extends CustomEnchantInstruction>> customEnchantInstructions = Map.of(
            "command", CommandInstruction.class,
            "delay", DelayInstruction.class,
            "repeat", RepeatInstruction.class,
            "save", SaveInstruction.class,
            "load", LoadInstruction.class,
            "conditional", ConditionalInstruction.class,
            "while", WhileInstruction.class
    );
    private static final Pattern pattern = Pattern.compile("(\\$\\[(?:(?!\\$\\[).)*?\\])");

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

    public String parseNestedExpression(String value, Player player, Map<String, Supplier<String>> parameters) {
        String substitutedValue = Util.replaceParameters(player, value, parameters);

        Matcher matcher = pattern.matcher(substitutedValue);
        while (matcher.find()) {
            String expressionString = matcher.group();
            String result = parseExpression(
                    expressionString.substring(2, expressionString.length() - 1),
                    EvaluationValue::getStringValue,
                    "error"
            );
            substitutedValue = substitutedValue.replace(expressionString, result);
            matcher = pattern.matcher(substitutedValue);
        }
        return substitutedValue;
    }

    private <T> T parseExpression(String expressionString, Function<EvaluationValue, T> getValue, T defaultValue) {
        Expression expression = new Expression(expressionString);

        T result = defaultValue;
        try {
            result = getValue.apply(expression.evaluate());
        } catch (ParseException | EvaluationException e) {
            Util.error("Error while evaluating following expression: " + expressionString + ", do all parameters exist?");
            Util.debug(e.getMessage());
        }
        return result;
    }

    protected abstract void setValue(Object value);

    protected void execute(Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        execute(player, parameters, executeNextInstruction);
    }

    protected void execute(Player player, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
    }

    protected boolean parseCondition(Player player, String value, Map<String, Supplier<String>> parameters) {
        String substitutedValue = parseNestedExpression(value, player, parameters);

        return parseExpression(substitutedValue, EvaluationValue::getBooleanValue, false);
    }

    protected Double parseDouble(Player player, String value, Map<String, Supplier<String>> parameters) {
        String substitutedValue = parseNestedExpression(value, player, parameters);
        return parseExpression(substitutedValue, EvaluationValue::getNumberValue, 0).doubleValue();
    }
}


