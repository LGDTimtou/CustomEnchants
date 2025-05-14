package com.lgdtimtou.customenchantments.enchantments.created.fields.instructions;

import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantInstruction;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Supplier;

public class DummyInstruction extends CustomEnchantInstruction {

    @Override
    protected void setValue(Object value) {
    }

    @Override
    protected void execute(Player player, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        executeNextInstruction.run();
    }
}
