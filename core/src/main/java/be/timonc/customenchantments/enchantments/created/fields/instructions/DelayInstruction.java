package be.timonc.customenchantments.enchantments.created.fields.instructions;

import be.timonc.customenchantments.Main;
import be.timonc.customenchantments.enchantments.created.fields.Instruction;
import be.timonc.customenchantments.enchantments.created.fields.InstructionCall;
import be.timonc.customenchantments.other.Util;
import org.bukkit.Bukkit;

import java.text.DecimalFormat;

public class DelayInstruction extends Instruction {

    private String delayTime;


    @Override
    protected void setValue(Object value) {
        this.delayTime = String.valueOf(value);
    }


    @Override
    protected void execute(InstructionCall instructionCall, Runnable executeNextInstruction) {
        double delay = parseDouble(delayTime, instructionCall.getPlayer(), instructionCall.getParameters());
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
