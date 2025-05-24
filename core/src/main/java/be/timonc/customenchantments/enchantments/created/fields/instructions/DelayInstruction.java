package be.timonc.customenchantments.enchantments.created.fields.instructions;

import be.timonc.customenchantments.Main;
import be.timonc.customenchantments.enchantments.created.fields.CustomEnchantInstruction;
import be.timonc.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.function.Supplier;

public class DelayInstruction extends CustomEnchantInstruction {

    private String delayTime;


    @Override
    protected void setValue(Object value) {
        this.delayTime = String.valueOf(value);
    }


    @Override
    protected void execute(Player player, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        double delay = parseDouble(player, delayTime, parameters);
        DecimalFormat df = new DecimalFormat("#.00");
        Util.debug("Delaying for: " + df.format(delay) + " seconds");
        Bukkit.getScheduler()
              .runTaskLater(
                      Main.getMain(),
                      executeNextInstruction,
                      Double.valueOf(delay * 20).longValue()
              );
    }
}
