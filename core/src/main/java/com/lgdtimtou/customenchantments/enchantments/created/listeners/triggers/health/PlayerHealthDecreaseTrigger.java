package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.health;

import com.lgdtimtou.customenchantments.customevents.health_decrease.PlayerHealthDecreaseEvent;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class PlayerHealthDecreaseTrigger extends Trigger {
    public PlayerHealthDecreaseTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
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
