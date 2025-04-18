package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.health;

import com.lgdtimtou.customenchantments.customevents.health_change.PlayerHealthChangeEvent;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class PlayerHealthChangeTrigger extends Trigger {
    public PlayerHealthChangeTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onHealthChange(PlayerHealthChangeEvent e) {
        executeCommands(e, e.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "health"),
                e.getHealth(),
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "health"),
                e.getHealth(),
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "health"),
                e.getHealth(),
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "previous_health"),
                e.getPrevious_health(),
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "previous_health"),
                e.getPrevious_health(),
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "previous_health"),
                e.getPrevious_health(),
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "health_change"),
                e.getHealthChange(),
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "health_change"),
                e.getHealthChange(),
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "health_change"),
                e.getHealthChange()
        ), Map.of());
    }
}
