package com.lgdtimtou.customenchantments.enchantments.created.values;

import java.util.Queue;

public record CustomEnchantLevel(int cooldown, double chance, boolean cancelEvent,
                                 Queue<CustomEnchantInstruction> instructions) {
}
