package com.lgdtimtou.customenchantments.enchantments.created.triggers.health;

import com.lgdtimtou.customenchantments.customevents.health_change.PlayerHealthDecreaseEvent;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class PlayerHealthDecreaseTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public PlayerHealthDecreaseTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onPlayerHealthDecrease(PlayerHealthDecreaseEvent e) {
        triggerInvoker.trigger(e, e.getPlayer(), Map.of(
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
                e.getDecrease(),
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "health_change"),
                e.getDecrease(),
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "health_change"),
                e.getDecrease()
        ), Map.of());
    }
}
