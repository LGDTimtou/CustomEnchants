package be.timonc.customenchantments.enchantments.created.fields.instructions;

import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.enchantments.created.fields.CustomEnchantInstruction;
import be.timonc.customenchantments.enchantments.created.fields.instructions.data.SaveContext;
import be.timonc.customenchantments.other.Util;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Supplier;

public class LoadInstruction extends CustomEnchantInstruction {

    private SaveContext context;
    private String identifier;
    private String defaultValue;

    @Override
    protected void setValue(Object value) {
        try {
            Map<String, String> values = (Map<String, String>) value;
            this.context = SaveContext.valueOf(values.get("context").toUpperCase());
            this.identifier = values.get("identifier");
            this.defaultValue = values.getOrDefault("default_value", "");
        } catch (Exception e) {
            Util.error("Error while parsing 'load' instruction: " + value);
        }
    }

    @Override
    protected void execute(Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        String value = context.load(
                player,
                customEnchant,
                identifier,
                parseNestedExpression(defaultValue, player, parameters)
        );
        parameters.put(identifier, () -> value);
        executeNextInstruction.run();
    }
}
