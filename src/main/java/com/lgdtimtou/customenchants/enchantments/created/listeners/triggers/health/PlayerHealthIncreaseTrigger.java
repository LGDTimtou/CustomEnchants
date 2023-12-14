package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.health;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Map;

public class PlayerHealthIncreaseTrigger extends Trigger {
    public PlayerHealthIncreaseTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
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
