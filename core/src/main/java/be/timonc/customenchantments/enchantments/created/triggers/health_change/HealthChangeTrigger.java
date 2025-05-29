package be.timonc.customenchantments.enchantments.created.triggers.health_change;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Map;
import java.util.Set;

public class HealthChangeTrigger extends TriggerListener {

    private final TriggerConditionGroup newHealthConditions = new TriggerConditionGroup(
            "new_health", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup previousHealthConditions = new TriggerConditionGroup(
            "previous_health", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup changeAmountConditions = new TriggerConditionGroup(
            "health_change", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup changeCauseConditions = new TriggerConditionGroup(
            "change", TriggerConditionGroupType.CAUSE
    );

    public HealthChangeTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        triggerInvoker.trigger(event, player, Map.of(
                newHealthConditions, player.getHealth() - event.getFinalDamage(),
                previousHealthConditions, player.getHealth(),
                changeAmountConditions, event.getFinalDamage(),
                changeCauseConditions, event.getCause()
        ));
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        triggerInvoker.trigger(event, player, Map.of(
                newHealthConditions, player.getHealth() + event.getAmount(),
                previousHealthConditions, player.getHealth(),
                changeAmountConditions, event.getAmount(),
                changeCauseConditions, event.getRegainReason()
        ));
    }


    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(newHealthConditions, previousHealthConditions, changeAmountConditions, changeCauseConditions);
    }
}
