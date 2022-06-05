package com.lgdtimtou.customenchants.other;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
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
                    Util.log(ChatColor.RED + "Check enchantments.yml for add function usage: $[add(double, double, ...)]");
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
                    Util.log(ChatColor.RED + "Check enchantments.yml for add function usage: $[add(double, double, ...)]");
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
                    Util.log(ChatColor.RED + "Check enchantments.yml for add function usage: $[add(double, double, ...)]");
                    return "-1";
                }
            }
            return String.valueOf(mul);
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
