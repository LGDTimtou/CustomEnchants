package be.timonc.customenchantments.enchantments.created.triggers.health;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Map;

public class PlayerHealthIncreaseTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public PlayerHealthIncreaseTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onHealthIncrease(EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        triggerInvoker.trigger(e, player, Map.of(
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
