package be.timonc.customenchantments.enchantments.custom.fields.instructions;

import be.timonc.customenchantments.enchantments.custom.fields.instructions.types.*;
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

public abstract class Instruction {

    private static final Map<String, Class<? extends Instruction>> customEnchantInstructions = Map.of(
            "command", CommandInstruction.class,
            "delay", DelayInstruction.class,
            "repeat", RepeatInstruction.class,
            "save", SaveInstruction.class,
            "load", LoadInstruction.class,
            "conditional", ConditionalInstruction.class,
            "while", WhileInstruction.class,
            "cancel", CancelInstruction.class
    );
    private static final Pattern pattern = Pattern.compile("(\\$\\[(?:(?!\\$\\[).)*?\\])");

    public static Instruction getInstruction(String name, Object value) {
        // Check if the instruction type exists in the map
        if (!customEnchantInstructions.containsKey(name)) {
            Util.warn("Instruction type '" + name + "' does not exist");
            return new DummyInstruction();
        }

        // Retrieve the instruction class
        Class<? extends Instruction> instructionClass = customEnchantInstructions.get(name);

        try {
            // Create an instance of the instruction class
            Instruction instruction = instructionClass.getDeclaredConstructor().newInstance();
            instruction.setValue(value);
            return instruction;
        } catch (Exception e) {
            Util.warn("Failed to create instruction: " + name + ". Error: " + e.getMessage());
            return new DummyInstruction();
        }
    }

    public static Queue<Instruction> parseInstructions(List<?> unparsedInstructions) {
        Queue<Instruction> instructionQueue = new ArrayDeque<>();

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

    public static String parseNestedExpression(String value, Player player, Map<String, Supplier<String>> parameters) {
        String substitutedValue = Util.replaceParameters(player, value, parameters);

        Matcher matcher = pattern.matcher(substitutedValue);
        while (matcher.find()) {
            String expressionString = matcher.group();
            String expressionStringContent = expressionString.substring(2, expressionString.length() - 1);
            String result = parseExpression(
                    expressionStringContent,
                    EvaluationValue::getStringValue,
                    expressionStringContent
            );
            substitutedValue = substitutedValue.replace(expressionString, result);
            matcher = pattern.matcher(substitutedValue);
        }
        return substitutedValue;
    }

    private static <T> T parseExpression(String expressionString, Function<EvaluationValue, T> getValue, T defaultValue) {
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

    public static boolean parseCondition(String value, Player player, Map<String, Supplier<String>> parameters) {
        String substitutedValue = parseNestedExpression(value, player, parameters);
        return parseExpression(substitutedValue, EvaluationValue::getBooleanValue, true);
    }

    protected static Double parseDouble(String value, Player player, Map<String, Supplier<String>> parameters) {
        String substitutedValue = parseNestedExpression(value, player, parameters);
        return parseExpression(substitutedValue, EvaluationValue::getNumberValue, 0).doubleValue();
    }

    protected abstract void setValue(Object value);

    protected abstract void execute(InstructionCall instructionCall, Runnable executeNextInstruction);
}


