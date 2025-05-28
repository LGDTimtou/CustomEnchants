package be.timonc.customenchantments.enchantments.created.triggers.take_damage;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.Set;

public class TakeDamageFromPlayerTrigger extends TriggerListener {

    private final TriggerConditionGroup attackerPlayerConditions = new TriggerConditionGroup(
            "attacker", TriggerConditionGroupType.PLAYER
    );
    private final TriggerConditionGroup damageConditions = new TriggerConditionGroup(
            "damage", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup damageCauseConditions = new TriggerConditionGroup(
            "damage", TriggerConditionGroupType.CAUSE
    );

    public TakeDamageFromPlayerTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onTakeDamageFromPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        if (!(e.getDamager() instanceof Player damager))
            return;
        triggerInvoker.trigger(e, player, Map.of(
                attackerPlayerConditions, damager,
                damageConditions, e.getDamage(),
                damageCauseConditions, e.getCause()
        ), Map.of());
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(attackerPlayerConditions, damageConditions, damageCauseConditions);
    }
}
