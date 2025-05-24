package be.timonc.customenchantments.enchantments.created.fields.instructions.data;

import be.timonc.customenchantments.other.Util;

import java.util.function.BiPredicate;

public enum BooleanOperation {
    EQUALS("==", Object::equals),
    NOT_EQUALS("!=", (a, b) -> !a.equals(b)),
    GREATER_THAN(">", (a, b) -> compare(a, b) > 0),
    GREATER_THAN_OR_EQUALS(">=", (a, b) -> compare(a, b) >= 0),
    LESS_THAN("<", (a, b) -> compare(a, b) < 0),
    LESS_THAN_OR_EQUALS("<=", (a, b) -> compare(a, b) <= 0);

    private final String symbol;
    private final BiPredicate<Object, Object> operation;

    BooleanOperation(String symbol, BiPredicate<Object, Object> operation) {
        this.symbol = symbol;
        this.operation = operation;
    }

    // Comparison method for Double and String values
    @SuppressWarnings("unchecked")
    private static int compare(Object a, Object b) {
        if (a instanceof Double && b instanceof Double) {
            return ((Double) a).compareTo((Double) b);
        } else if (a instanceof String && b instanceof String) {
            return ((String) a).compareTo((String) b);
        }
        Util.error("Cannot compare values of different types: " + a.getClass() + " and " + b.getClass());
        return 0;
    }

    public boolean apply(Object a, Object b) {
        return operation.test(a, b);
    }

    @Override
    public String toString() {
        return symbol;
    }
}
