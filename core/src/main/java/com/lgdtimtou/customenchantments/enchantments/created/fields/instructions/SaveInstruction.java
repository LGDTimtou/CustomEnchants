package com.lgdtimtou.customenchantments.enchantments.created.fields.instructions;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantInstruction;
import com.lgdtimtou.customenchantments.enchantments.created.fields.instructions.data.SaveContext;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Supplier;

public class SaveInstruction extends CustomEnchantInstruction {

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
        String parsedValue = parseNestedExpression(player, value, parameters);
        this.context.save(player, customEnchant, identifier, parsedValue);
        executeNextInstruction.run();
    }
}
