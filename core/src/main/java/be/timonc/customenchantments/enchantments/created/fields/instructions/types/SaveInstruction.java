package be.timonc.customenchantments.enchantments.created.fields.instructions.types;

import be.timonc.customenchantments.enchantments.created.fields.instructions.Instruction;
import be.timonc.customenchantments.enchantments.created.fields.instructions.InstructionCall;
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
    protected void execute(InstructionCall instructionCall, Runnable executeNextInstruction) {
        Player player = instructionCall.getPlayer();
        Map<String, Supplier<String>> parameters = instructionCall.getParameters();
        String parsedIdentifier = parseNestedExpression(identifier, player, parameters);
        String parsedValue = parseNestedExpression(value, player, parameters);
        this.context.save(player, instructionCall.getCustomEnchant(), parsedIdentifier, parsedValue);
        parameters.put(parsedIdentifier, () -> parsedValue);
        executeNextInstruction.run();
    }
}
