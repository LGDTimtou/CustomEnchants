package com.lgdtimtou.customenchantments.enchantments.created.fields.instructions;

import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantInstruction;
import com.lgdtimtou.customenchantments.other.CustomEnchantLogFilter;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Supplier;

public class CommandInstruction extends CustomEnchantInstruction {

    private String command;


    @Override
    protected void setValue(Object value) {
        this.command = String.valueOf(value);
    }

    @Override
    protected void execute(Player player, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        String executableCommand = parseNestedExpression(player, command, parameters);
        Util.debug("Running command: " + executableCommand);

        CustomEnchantLogFilter.addFilter();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executableCommand);

        CustomEnchantLogFilter.removeFilter();

        executeNextInstruction.run();
    }
}
