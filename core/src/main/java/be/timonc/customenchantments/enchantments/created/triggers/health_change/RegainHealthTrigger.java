package be.timonc.customenchantments.enchantments.created.triggers.health_change;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Map;
import java.util.Set;

public class RegainHealthTrigger extends TriggerListener {

    private final TriggerConditionGroup newHealthConditions = new TriggerConditionGroup(
            "new_health", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup previousHealthConditions = new TriggerConditionGroup(
            "previous_health", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup increaseAmountConditions = new TriggerConditionGroup(
            "health_regain", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup regainCauseConditions = new TriggerConditionGroup(
            "regain", TriggerConditionGroupType.CAUSE
    );

    public RegainHealthTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }

    @EventHandler
    public void onHealthIncrease(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        triggerInvoker.trigger(event, player, Map.of(
                newHealthConditions,
                player.getHealth() + event.getAmount(),
                previousHealthConditions,
                player.getHealth(),
                increaseAmountConditions,
                event.getAmount(),
                regainCauseConditions,
                event.getRegainReason()
        ));
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(newHealthConditions, previousHealthConditions, increaseAmountConditions, regainCauseConditions);
    }
}
