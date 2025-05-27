package be.timonc.customenchantments.enchantments.created.triggers.take_damage;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;

public class TakeDamageFromNonEntityTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public TakeDamageFromNonEntityTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onTakeDamageFromNonEntity(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;

        triggerInvoker.trigger(
                e,
                player,
                Map.of(new ConditionKey(TriggerConditionType.CAUSE, "cause"), e.getCause()),
                Map.of()
        );
    }
}
