package be.timonc.customenchantments.enchantments.custom.fields.instructions.types;

import be.timonc.customenchantments.enchantments.custom.fields.instructions.Instruction;
import be.timonc.customenchantments.enchantments.custom.fields.instructions.InstructionCall;
import be.timonc.customenchantments.enchantments.custom.fields.instructions.data.SaveContext;
import be.timonc.customenchantments.other.Util;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Supplier;

public class LoadInstruction extends Instruction {

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
    protected void execute(InstructionCall instructionCall, Runnable executeNextInstruction) {
        Player player = instructionCall.getPlayer();
        Map<String, Supplier<String>> parameters = instructionCall.getParameters();
        String parsedIdentifier = parseNestedExpression(identifier, player, parameters);
        String value = context.load(
                player,
                instructionCall.getCustomEnchant(),
                parsedIdentifier,
                parseNestedExpression(defaultValue, player, parameters)
        );
        parameters.put(parsedIdentifier, () -> value);
        executeNextInstruction.run();
    }
}
