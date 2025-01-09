package com.lgdtimtou.customenchantments.other;

import org.bukkit.ChatColor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum FileFunction {

    ADD("add") {
        @Override
        public String execute(String... values) {
            if (values.length == 0)
                return "0";
            else if (values.length == 1)
                return values[0];

            double sum = 0;
            for (String value : values){
                try {
                    sum += Double.parseDouble(value);
                } catch (NumberFormatException e){
                    Util.log(ChatColor.RED + "Check enchantments.yml: wrong 'add' function usage: $[add(value1, value2, ...)]");
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
            for (String value : values){
                try {
                    sub -= first ? -Double.parseDouble(value) : Double.parseDouble(value);
                    first = false;
                } catch (NumberFormatException e){
                    Util.log(ChatColor.RED + "Check enchantments.yml: wrong 'sub' function usage: $[sub(value1, value2, ...)]");
                    return "-1";
                }
            }
            return String.valueOf(sub);
        }
    },
    MUL("mul"){
        @Override
        public String execute(String... values) {
            if (values.length == 0)
                return "0";
            else if (values.length == 1)
                return values[0];

            double mul = 1;
            for (String value : values){
                try {
                    mul *= Double.parseDouble(value);
                } catch (NumberFormatException e){
                    Util.error("Check enchantments.yml: wrong 'mul' function usage: $[mul(value1, value2, ...)]");
                    return "-1";
                }
            }
            return String.valueOf(mul);
        }
    },
    DIV("div"){
        @Override
        public String execute(String... values) {
            if (values.length == 0)
                return "0";

            try {
                double div = Double.parseDouble(values[0]);
                for (int i = 1; i < values.length; i++) {
                    double val = Double.parseDouble(values[i]);
                    if (val == 0) {
                        Util.error("Division by zero in 'div' function: $[div(value1, value2, ...)]");
                        return "-1";
                    }
                    div /= val;
                }
                return String.valueOf(div);
            } catch (NumberFormatException e) {
                Util.error("Check enchantments.yml: wrong 'div' function usage: $[div(value1, value2, ...)]");
                return "-1";
            }
        }
    },
    RANDOM("random"){
        @Override
        public String execute(String... values) {
            return String.valueOf(Math.random());
        }
    },
    ROUND("round") {
        @Override
        public String execute(String... values) {
            if (values.length < 1) {
                Util.error("Check enchantments.yml: wrong 'round' function usage: $[round(value, decimalPlaces)]");
                return "-1";
            }

            try {
                double number = Double.parseDouble(values[0]);
                int decimalPlaces = 0;

                if (values.length > 1) {
                    decimalPlaces = Integer.parseInt(values[1]);
                }

                double scale = Math.pow(10, decimalPlaces);
                double roundedValue = Math.round(number * scale) / scale;

                return String.valueOf(roundedValue);
            } catch (NumberFormatException e) {
                Util.error("Check enchantments.yml: wrong 'round' function usage: $[round(value, decimalPlaces)]");
                return "-1";
            }
        }
    };

    private final String functionName;

    FileFunction(String functionName){
        this.functionName = functionName;
    }

    public String getName() {
        return functionName;
    }

    public abstract String execute(String... values);


    public static List<String> parse(List<String> commands) throws NumberFormatException{
        return commands.stream().map(c -> {
            String updated = c;
            Matcher m = Pattern.compile("\\$\\[[^]$]*]").matcher(updated);
            while (m.find()){
                String find = m.group();
                Matcher mFunction = Pattern.compile("[a-z_]+").matcher(find);
                if (mFunction.find()){
                    String functionName = mFunction.group();
                    if (Arrays.stream(values()).noneMatch(v -> v.getName().equalsIgnoreCase(functionName))){
                        Util.log(ChatColor.RED + functionName + " function does not exist, check your enchantments.yml file");
                        return c;
                    }
                    FileFunction function = FileFunction.valueOf(functionName.toUpperCase());
                    updated = updated.replace(find, function.execute(find.replaceAll("^[^(]+\\(", "").replaceAll("\\)]", "").split("[ ]*,[ ]*")));
                }
                m = Pattern.compile("\\$\\[[^]$]*]").matcher(updated);
            }
            return updated;
        }).collect(Collectors.toList());
    }
}
