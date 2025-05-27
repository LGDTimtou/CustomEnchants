package be.timonc.customenchantments.enchantments.created.triggers.take_damage;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class TakeDamageFromPlayerTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public TakeDamageFromPlayerTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onTakeDamageFromPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        if (!(e.getDamager() instanceof Player damager))
            return;
        triggerInvoker.trigger(e, player, Map.of(
                new ConditionKey(TriggerConditionType.PLAYER, "damager"), damager,
                new ConditionKey(TriggerConditionType.CAUSE, "damage"), e.getCause()
        ), Map.of());
    }
}
