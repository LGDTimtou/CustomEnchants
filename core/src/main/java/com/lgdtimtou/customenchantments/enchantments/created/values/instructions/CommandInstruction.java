package com.lgdtimtou.customenchantments.enchantments.created.values.instructions;

import com.lgdtimtou.customenchantments.enchantments.created.values.CustomEnchantInstruction;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.function.Supplier;

public class CommandInstruction extends CustomEnchantInstruction {

    private String command;


    @Override
    protected void setValue(Object value) {
        this.command = String.valueOf(value);
    }

    @Override
    protected void execute(Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.parseValue(command, parameters));

        executeNextInstruction.run();
    }
}
