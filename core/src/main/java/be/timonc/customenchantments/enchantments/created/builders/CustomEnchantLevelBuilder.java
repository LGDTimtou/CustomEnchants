package be.timonc.customenchantments.enchantments.created.builders;

import be.timonc.customenchantments.enchantments.created.fields.Instruction;
import be.timonc.customenchantments.enchantments.created.fields.Level;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;


public class CustomEnchantLevelBuilder {

    private final double cooldown;
    private final boolean cancelEvent;
    private final Queue<Instruction> instructions;
    private double chance;
    private String cooldownMessage;

    public CustomEnchantLevelBuilder(ConfigurationSection section, Level previous) {
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
                    Instruction.parseInstructions(unparsedInstructions);
        }
    }

    public CustomEnchantLevelBuilder() {
        this.cooldown = 0;
        this.cooldownMessage = "";
        this.chance = 100;
        this.cancelEvent = false;
        this.instructions = new ArrayDeque<>();
    }

    public Level build() {
        return new Level(cooldown, cooldownMessage, chance, cancelEvent, instructions);
    }
}
