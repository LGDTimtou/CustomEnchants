package com.lgdtimtou.customenchantments.enchantments.created.fields;

import java.util.Queue;

public record CustomEnchantLevel(double cooldown, String cooldownMessage, double chance, boolean cancelEvent,
                                 Queue<CustomEnchantInstruction> instructions) {
}
