package be.timonc.customenchantments.enchantments.custom.triggers.click;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class LeftClickTrigger extends TriggerListener {


    public LeftClickTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK) return;

        triggerInvoker.trigger(e, e.getPlayer());
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of();
    }
}

