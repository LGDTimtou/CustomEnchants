package be.timonc.customenchantments.enchantments.custom.builders;

import be.timonc.customenchantments.enchantments.custom.fields.Level;
import be.timonc.customenchantments.enchantments.custom.fields.instructions.Instruction;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class CustomEnchantLevelBuilder {

    private final double cooldown;
    private final boolean cancelEvent;
    private final Queue<Instruction> instructions;
    private final List<String> cleanupCommands;
    private double chance;
    private String cooldownMessage;

    public CustomEnchantLevelBuilder(ConfigurationSection section, Level previous) {
        if (section == null) {
            cooldown = previous.cooldown();
            chance = previous.chance();
            cancelEvent = previous.cancelEvent();
            cooldownMessage = previous.cooldownMessage();
            instructions = previous.instructions();
            cleanupCommands = previous.cleanupCommands();
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
            cleanupCommands = section.getStringList("cleanup_commands");

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
        this.cleanupCommands = new ArrayList<>();
    }

    public Level build() {
        return new Level(cooldown, cooldownMessage, chance, cancelEvent, instructions, cleanupCommands);
    }
}
