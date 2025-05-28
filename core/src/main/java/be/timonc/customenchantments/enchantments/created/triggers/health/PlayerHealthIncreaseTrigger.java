package be.timonc.customenchantments.enchantments.created.triggers.health;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Map;
import java.util.Set;

public class PlayerHealthIncreaseTrigger extends TriggerListener {

    private final TriggerConditionGroup healthConditions = new TriggerConditionGroup(
            "health", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup previousHealthConditions = new TriggerConditionGroup(
            "previous_health", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup increaseAmountConditions = new TriggerConditionGroup(
            "increase_amount", TriggerConditionGroupType.NUMBER
    );

    public PlayerHealthIncreaseTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onHealthIncrease(EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        triggerInvoker.trigger(e, player, Map.of(
                healthConditions,
                player.getHealth() + e.getAmount(),
                previousHealthConditions,
                player.getHealth(),
                increaseAmountConditions,
                e.getAmount()
        ));
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(healthConditions, previousHealthConditions, increaseAmountConditions);
    }
}
