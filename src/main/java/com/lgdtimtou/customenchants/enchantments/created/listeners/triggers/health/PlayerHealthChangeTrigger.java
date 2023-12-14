package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.health;

import com.lgdtimtou.customenchants.customevents.health_change.PlayerHealthChangeEvent;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class PlayerHealthChangeTrigger extends Trigger {
    public PlayerHealthChangeTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
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
