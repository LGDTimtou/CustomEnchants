package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.health;

import com.lgdtimtou.customenchantments.customevents.health_change.PlayerHealthChangeEvent;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class PlayerHealthChangeTrigger extends Trigger {
    public PlayerHealthChangeTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onHealthChange(PlayerHealthChangeEvent e){
        executeCommands(e, e.getPlayer(), String.valueOf(e.getHealth()), Map.of(
                "health", String.valueOf(e.getHealth()),
                "health_change", String.valueOf(e.getHealthChange()),
                "previous_health", String.valueOf(e.getPrevious_health())
        ));
    }


}
