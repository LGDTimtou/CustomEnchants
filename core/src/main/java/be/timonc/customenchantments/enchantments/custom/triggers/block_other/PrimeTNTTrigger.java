package be.timonc.customenchantments.enchantments.custom.triggers.block_other;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.TNTPrimeEvent;

import java.util.Map;
import java.util.Set;

public class PrimeTNTTrigger extends TriggerListener {

    private final TriggerConditionGroup primeCauseConditions = new TriggerConditionGroup(
            "prime", TriggerConditionGroupType.CAUSE
    );

    public PrimeTNTTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onPrimeTNT(TNTPrimeEvent e) {
        if (!(e.getPrimingEntity() instanceof Player player)) return;

        triggerInvoker.trigger(
                e,
                player,
                Map.of(primeCauseConditions, e.getCause())
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(primeCauseConditions);
    }
}
