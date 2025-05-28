package be.timonc.customenchantments.enchantments.created.fields.triggers.conditions;

import be.timonc.customenchantments.other.Util;

import java.util.function.Function;
import java.util.regex.Pattern;

public class TriggerConditionValue {

    private final Operator operator;
    private final String value;

    public TriggerConditionValue(Operator operator, String value) {
        this.operator = operator;
        this.value = value;
    }


    public boolean check(Object toCompare) {
        if (toCompare instanceof Double toCompareDouble) {
            try {
                Double doubleValue = Double.parseDouble(value);
                return operator.checkResult(toCompareDouble.compareTo(doubleValue));
            } catch (NumberFormatException e) {
                Util.error("Could not parse trigger condition value " + value + " to a number!");
                return true;
            }
        } else {
            String toCompareString = toCompare.toString();
            if (operator == Operator.EQUALS || operator == Operator.NOT_EQUALS) {
                boolean result = Pattern.compile(toCompareString.toLowerCase().replace("*", ".*"))
                                        .matcher(value.toLowerCase())
                                        .matches();

                return (operator == Operator.EQUALS) == result;
            } else {
                return operator.checkResult(toCompareString.compareTo(value));
            }
        }
    }


    public enum Operator {
        EQUALS(value -> value == 0),
        NOT_EQUALS(value -> value != 0),
        GREATER_THAN(value -> value > 0),
        LESS_THAN(value -> value < 0),
        GREATER_THAN_OR_EQUALS(value -> value >= 0),
        LESS_THAN_OR_EQUALS(value -> value <= 0);

        private final Function<Integer, Boolean> checkCompareResult;

        Operator(Function<Integer, Boolean> checkCompareResult) {
            this.checkCompareResult = checkCompareResult;
        }

        private boolean checkResult(int result) {
            return checkCompareResult.apply(result);
        }
    }
}
