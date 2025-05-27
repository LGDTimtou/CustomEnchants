package be.timonc.customenchantments.enchantments.created.fields.instructions;

import be.timonc.customenchantments.enchantments.created.fields.Instruction;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Supplier;

public class DummyInstruction extends Instruction {

    @Override
    protected void setValue(Object value) {
    }

    @Override
    protected void execute(Player player, Map<String, Supplier<String>> parameters, Runnable executeNextInstruction) {
        executeNextInstruction.run();
    }
}
