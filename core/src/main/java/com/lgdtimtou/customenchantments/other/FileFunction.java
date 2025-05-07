package com.lgdtimtou.customenchantments.other;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum FileFunction {

    ADD("add") {
        @Override
        public String execute(String... values) {
            if (values.length == 0)
                return "0";
            else if (values.length == 1)
                return values[0];

            double sum = 0;
            for (String value : values) {
                try {
                    sum += Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    Util.error("Invalid value '" + value + "' in 'add' function. Ensure all values are numeric: $[add(value1, value2, ...)]");
                    return "-1";
                }
            }
            return String.valueOf(sum);
        }
    },
    SUB("sub") {
        @Override
        public String execute(String... values) {
            if (values.length == 0)
                return "0";
            else if (values.length == 1)
                return values[0];

            double sub = 0;
            boolean first = true;
            for (String value : values) {
                try {
                    sub -= first ? -Double.parseDouble(value) : Double.parseDouble(value);
                    first = false;
                } catch (NumberFormatException e) {
                    Util.error("Invalid value '" + value + "' in 'sub' function. Ensure all values are numeric: $[sub(value1, value2, ...)]");
                    return "-1";
                }
            }
            return String.valueOf(sub);
        }
    },
    MUL("mul") {
        @Override
        public String execute(String... values) {
            if (values.length == 0)
                return "0";
            else if (values.length == 1)
                return values[0];

            double mul = 1;
            for (String value : values) {
                try {
                    mul *= Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    Util.error("Invalid value '" + value + "' in 'mul' function. Ensure all values are numeric: $[mul(value1, value2, ...)]");
                    return "-1";
                }
            }
            return String.valueOf(mul);
        }
    },
    DIV("div") {
        @Override
        public String execute(String... values) {
            if (values.length == 0)
                return "0";

            String stringValue = values[0];
            try {
                double div = Double.parseDouble(stringValue);
                for (int i = 1; i < values.length; i++) {
                    stringValue = values[i];
                    double val = Double.parseDouble(stringValue);
                    if (val == 0) {
                        Util.error(
                                "Division by zero in 'div' function. Ensure no divisor is zero: $[div(value1, value2, ...)]");
                        return "-1";
                    }
                    div /= val;
                }
                return String.valueOf(div);
            } catch (NumberFormatException e) {
                Util.error("Invalid value '" + stringValue + "' in 'div' function. Ensure all values are numeric: $[div(value1, value2, ...)]");
                return "-1";
            }
        }
    },
    RANDOM("random") {
        @Override
        public String execute(String... values) {
            return String.valueOf(Math.random());
        }
    },
    ROUND("round") {
        @Override
        public String execute(String... values) {
            if (values.length < 1) {
                Util.error("Missing value in 'round' function. Usage: $[round(value, decimalPlaces)]");
                return "-1";
            }

            String stringValue = values[0];

            try {
                double number = Double.parseDouble(stringValue);
                int decimalPlaces = 0;

                if (values.length > 1) {
                    stringValue = values[1];
                    decimalPlaces = Integer.parseInt(stringValue);
                }

                double scale = Math.pow(10, decimalPlaces);
                double roundedValue = Math.round(number * scale) / scale;

                return String.valueOf(roundedValue);
            } catch (NumberFormatException e) {
                Util.error("Invalid value '" + stringValue + "' in 'round' function. Ensure the first value is numeric and the second (if present) is an integer: $[round(value, decimalPlaces)]");
                return "-1";
            }
        }
    };

    private final String functionName;

    FileFunction(String functionName) {
        this.functionName = functionName;
    }

    public static String parse(String command) {
        String updated = command;
        Matcher m = Pattern.compile("\\$\\[[^]$]*]").matcher(updated);
        while (m.find()) {
            String find = m.group();
            Matcher mFunction = Pattern.compile("[a-z_]+").matcher(find);
            if (mFunction.find()) {
                String functionName = mFunction.group();
                if (Arrays.stream(values()).noneMatch(v -> v.getName().equalsIgnoreCase(functionName))) {
                    Util.error("Unknown function '" + functionName + "' in 'parse'. Check your enchantments.yml file.");
                    return command;
                }
                FileFunction function = FileFunction.valueOf(functionName.toUpperCase());
                updated = updated.replace(
                        find,
                        function.execute(find.replaceAll("^[^(]+\\(", "").replaceAll("\\)]", "").split("[ ]*,[ ]*"))
                );
            }
            m = Pattern.compile("\\$\\[[^]$]*]").matcher(updated);
        }
        return updated;
    }

    public String getName() {
        return functionName;
    }

    public abstract String execute(String... values);
}
