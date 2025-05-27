package be.timonc.customenchantments.enchantments.created.fields;

import java.util.Queue;

public record Level(double cooldown, String cooldownMessage, double chance, boolean cancelEvent,
                    Queue<Instruction> instructions) {
}
