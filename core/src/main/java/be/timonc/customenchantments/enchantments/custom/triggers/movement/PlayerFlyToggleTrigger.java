package be.timonc.customenchantments.enchantments.custom.triggers.movement;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.Set;

public class PlayerFlyToggleTrigger extends TriggerListener {

    public PlayerFlyToggleTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }

    @EventHandler
    public void onToggleFly(PlayerToggleFlightEvent event) {
        triggerInvoker.trigger(event, event.getPlayer());
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of();
    }
}
