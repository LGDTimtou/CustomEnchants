package be.timonc.customenchantments.enchantments.custom.triggers.block_other;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BellRingEvent;

import java.util.Set;

public class BellRungTrigger extends TriggerListener {


    public BellRungTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }

    @EventHandler
    public void onBellRing(BellRingEvent e) {
        if (e.getEntity() == null || !(e.getEntity() instanceof Player player)) return;
        triggerInvoker.trigger(e, player);
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of();
    }
}
