package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.health;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Map;

public class PlayerHealthIncreaseTrigger extends Trigger {
    public PlayerHealthIncreaseTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onHealthIncrease(EntityRegainHealthEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;
        executeCommands(e, player, String.valueOf(player.getHealth() + e.getAmount()), Map.of(
                "health", String.valueOf(player.getHealth() + e.getAmount()),
                "health_change", String.valueOf(e.getAmount()),
                "previous_health", String.valueOf(player.getHealth())
        ));
    }
}
