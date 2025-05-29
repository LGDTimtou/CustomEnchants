package be.timonc.customenchantments.enchantments.created.triggers.movement;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.Set;

public class PlayerFlyStopTrigger extends TriggerListener {

    public PlayerFlyStopTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }

    @EventHandler
    public void onToggleFly(PlayerToggleFlightEvent event) {
        if (event.isFlying()) return;
        triggerInvoker.trigger(event, event.getPlayer());
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of();
    }
}
