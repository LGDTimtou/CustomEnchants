package be.timonc.customenchantments.enchantments.created.triggers.health_change;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;
import java.util.Set;

public class TakeDamageFromNonEntityTrigger extends TriggerListener {

    private final TriggerConditionGroup newHealthConditions = new TriggerConditionGroup(
            "new_health", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup previousHealthConditions = new TriggerConditionGroup(
            "previous_health", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup damageAmountConditions = new TriggerConditionGroup(
            "damage", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup damageCauseConditions = new TriggerConditionGroup(
            "damage", TriggerConditionGroupType.CAUSE
    );

    public TakeDamageFromNonEntityTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onTakeDamageFromNonEntity(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event instanceof EntityDamageByEntityEvent) return;

        triggerInvoker.trigger(
                event,
                player,
                Map.of(
                        newHealthConditions, player.getHealth() - event.getFinalDamage(),
                        previousHealthConditions, player.getHealth(),
                        damageAmountConditions, event.getFinalDamage(),
                        damageCauseConditions, event.getCause()
                ),
                Map.of()
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(newHealthConditions, previousHealthConditions, damageAmountConditions, damageCauseConditions);
    }
}
