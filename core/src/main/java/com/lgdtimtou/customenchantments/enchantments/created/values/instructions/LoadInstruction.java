package com.lgdtimtou.customenchantments.enchantments.created.values.instructions;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.values.CustomEnchantInstruction;
import com.lgdtimtou.customenchantments.enchantments.created.values.instructions.data.SaveContext;
import com.lgdtimtou.customenchantments.other.Util;
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
            this.defaultValue = values.getOrDefault("defaultValue", "");
        } catch (Exception e) {
            Util.error("Error while parsing 'load' instruction: " + value);
        }
    }

    @Override
    protected void execute(Player player, CustomEnchant customEnchant, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        String value = context.load(player, customEnchant, identifier, defaultValue);
        parameters.put(identifier, () -> value);
        executeNextInstruction.run();
    }
}
