package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.health;

import com.lgdtimtou.customenchants.customevents.health_decrease.PlayerHealthDecreaseEvent;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class PlayerHealthDecreaseTrigger extends Trigger {
    public PlayerHealthDecreaseTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
    }

    @EventHandler
    public void onPlayerHealthDecrease(PlayerHealthDecreaseEvent e){
        executeCommands(e, e.getPlayer(), String.valueOf(e.getHealth()), Map.of(
                "health", String.valueOf(e.getHealth()),
                "decrease", String.valueOf(e.getDecrease()),
                "previous_health", String.valueOf(e.getPrevious_health())
        ));
    }
}
