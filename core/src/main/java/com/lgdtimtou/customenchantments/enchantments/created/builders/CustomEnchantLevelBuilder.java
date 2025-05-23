package com.lgdtimtou.customenchantments.enchantments.created.builders;

import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantInstruction;
import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantLevel;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;


public class CustomEnchantLevelBuilder {

    private final double cooldown;
    private final boolean cancelEvent;
    private final Queue<CustomEnchantInstruction> instructions;
    private double chance;
    private String cooldownMessage;

    public CustomEnchantLevelBuilder(ConfigurationSection section, CustomEnchantLevel previous) {
        if (section == null) {
            cooldown = previous.cooldown();
            chance = previous.chance();
            cancelEvent = previous.cancelEvent();
            cooldownMessage = previous.cooldownMessage();
            instructions = previous.instructions();
        } else {
            cooldown = section.getDouble("cooldown", previous.cooldown());
            chance = section.getDouble("chance", previous.chance());
            if (chance > 100 || chance <= 0) chance = 100;
            cancelEvent = section.getBoolean("cancel_event", previous.cancelEvent());

            cooldownMessage = section.getString("cooldown_message", previous.cooldownMessage());
            cooldownMessage = cooldownMessage != null ? ChatColor.translateAlternateColorCodes(
                    '&',
                    cooldownMessage
            ) : null;

            List<?> unparsedInstructions = section.getList("instructions");
            this.instructions = unparsedInstructions == null || unparsedInstructions.isEmpty() ?
                    previous.instructions() :
                    CustomEnchantInstruction.parseInstructions(unparsedInstructions);
        }
    }

    public CustomEnchantLevelBuilder() {
        this.cooldown = 0;
        this.cooldownMessage = "";
        this.chance = 100;
        this.cancelEvent = false;
        this.instructions = new ArrayDeque<>();
    }

    public CustomEnchantLevel build() {
        return new CustomEnchantLevel(cooldown, cooldownMessage, chance, cancelEvent, instructions);
    }
}
