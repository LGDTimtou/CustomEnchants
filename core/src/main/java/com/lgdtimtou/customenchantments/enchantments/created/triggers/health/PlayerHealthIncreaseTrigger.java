package com.lgdtimtou.customenchantments.enchantments.created.triggers.health;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Map;

public class PlayerHealthIncreaseTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public PlayerHealthIncreaseTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onHealthIncrease(EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        triggerType.trigger(e, player, Map.of(
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
