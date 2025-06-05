package be.timonc.customenchantments.enchantments.custom.fields;

import be.timonc.customenchantments.enchantments.custom.fields.instructions.Instruction;

import java.util.List;
import java.util.Queue;

public record Level(double cooldown, String cooldownMessage, double chance, boolean cancelEvent,
                    Queue<Instruction> instructions, List<String> cleanupCommands) {
}
