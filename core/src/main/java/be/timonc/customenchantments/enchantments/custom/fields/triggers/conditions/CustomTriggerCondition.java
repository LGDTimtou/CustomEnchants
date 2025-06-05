package be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions;

import be.timonc.customenchantments.enchantments.custom.fields.instructions.Instruction;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Supplier;

public class CustomTriggerCondition {

    private final String condition;

    public CustomTriggerCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }


    public boolean check(Player player, Map<String, Supplier<String>> parameters) {
        return Instruction.parseCondition(condition, player, parameters);
    }
}
