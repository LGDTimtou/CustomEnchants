package com.lgdtimtou.customenchantments.enchantments.created.fields.instructions;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantInstruction;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.function.Supplier;

public class DelayInstruction extends CustomEnchantInstruction {

    private String delayTime;


    @Override
    protected void setValue(Object value) {
        this.delayTime = String.valueOf(value);
    }


    @Override
    protected void execute(Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        Double delay = parseValueAsDouble(delayTime, parameters);
        Util.debug("Delaying for: " + Math.round(delay) + " seconds");
        Bukkit.getScheduler()
              .runTaskLater(
                      Main.getMain(),
                      executeNextInstruction,
                      Double.valueOf(delay * 20).longValue()
              );
    }
}
