package be.timonc.customenchantments.enchantments.created.fields.instructions;

import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.enchantments.created.fields.Instruction;
import be.timonc.customenchantments.enchantments.created.fields.instructions.data.SaveContext;
import be.timonc.customenchantments.other.Util;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Supplier;

public class SaveInstruction extends Instruction {

    private SaveContext context;
    private String identifier;
    private String value;


    @Override
    protected void setValue(Object value) {
        try {
            Map<String, String> values = (Map<String, String>) value;
            this.context = SaveContext.valueOf(values.get("context").toUpperCase());
            this.identifier = values.get("identifier");
            this.value = values.get("value");
        } catch (Exception e) {
            Util.error("Error while parsing 'save' instruction: " + value);
        }
    }

    @Override
    protected void execute(Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        String parsedIdentifier = parseNestedExpression(identifier, player, parameters);
        String parsedValue = parseNestedExpression(value, player, parameters);
        this.context.save(player, customEnchant, parsedIdentifier, parsedValue);
        parameters.put(parsedIdentifier, () -> parsedValue);
        executeNextInstruction.run();
    }
}
