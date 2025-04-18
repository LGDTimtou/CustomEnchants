package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.health;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Map;

public class PlayerHealthIncreaseTrigger extends Trigger {
    public PlayerHealthIncreaseTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onHealthIncrease(EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        executeCommands(e, player, Map.of(
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "health"),
                player.getHealth() + e.getAmount(),
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "health"),
                player.getHealth() + e.getAmount(),
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "health"),
                player.getHealth() + e.getAmount(),
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "previous_health"),
                player.getHealth(),
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "previous_health"),
                player.getHealth(),
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "previous_health"),
                player.getHealth(),
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "health_change"),
                e.getAmount(),
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "health_change"),
                e.getAmount(),
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "health_change"),
                e.getAmount()
        ), Map.of());
    }
}
