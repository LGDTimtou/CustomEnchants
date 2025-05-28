package be.timonc.customenchantments.enchantments.created.triggers.health;

import be.timonc.customenchantments.customevents.health_change.PlayerHealthDecreaseEvent;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.event.EventHandler;

import java.util.Map;
import java.util.Set;

public class PlayerHealthDecreaseTrigger extends TriggerListener {

    private final TriggerConditionGroup healthConditions = new TriggerConditionGroup(
            "health", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup previousHealthConditions = new TriggerConditionGroup(
            "previous_health", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup decreaseAmountConditions = new TriggerConditionGroup(
            "decrease_amount", TriggerConditionGroupType.NUMBER
    );

    public PlayerHealthDecreaseTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onPlayerHealthDecrease(PlayerHealthDecreaseEvent e) {
        triggerInvoker.trigger(e, e.getPlayer(), Map.of(
                healthConditions,
                e.getHealth(),
                previousHealthConditions,
                e.getPrevious_health(),
                decreaseAmountConditions,
                e.getDecrease()
        ));
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(healthConditions, previousHealthConditions, decreaseAmountConditions);
    }
}
