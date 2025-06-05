package be.timonc.customenchantments.enchantments.custom.triggers.click;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class RightClickAirTrigger extends TriggerListener {


    public RightClickAirTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }

    @EventHandler
    public void onPlayerRightClickAir(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        triggerInvoker.trigger(event, event.getPlayer());
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of();
    }
}
