package com.lgdtimtou.customenchantments.enchantments.created;

import com.lgdtimtou.customenchantments.enchantments.created.values.CustomEnchantInstruction;
import com.lgdtimtou.customenchantments.enchantments.created.values.CustomEnchantLevel;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;


public class CustomEnchantLevelBuilder {

    private final int cooldown;
    private final double chance;
    private final boolean cancelEvent;
    private final Queue<CustomEnchantInstruction> instructions;

    public CustomEnchantLevelBuilder(ConfigurationSection section, CustomEnchantLevel previous) {
        int cooldown = section.getInt("cooldown", previous.cooldown());
        double chance = section.getDouble("chance", previous.chance());
        if (chance > 100 || chance <= 0) chance = 100;
        boolean cancelEvent = section.getBoolean("cancel_event", previous.cancelEvent());
        List<?> unparsedInstructions = section.getList("instructions");
        Queue<CustomEnchantInstruction> instructions = unparsedInstructions == null || unparsedInstructions.isEmpty() ?
                previous.instructions() :
                CustomEnchantInstruction.parseInstructions(unparsedInstructions);

        this.cooldown = cooldown;
        this.chance = chance;
        this.cancelEvent = cancelEvent;
        this.instructions = instructions;
    }

    public CustomEnchantLevelBuilder() {
        this.cooldown = 0;
        this.chance = 100;
        this.cancelEvent = false;
        this.instructions = new ArrayDeque<>();
    }

    public CustomEnchantLevel build() {
        return new CustomEnchantLevel(cooldown, chance, cancelEvent, instructions);
    }
}
