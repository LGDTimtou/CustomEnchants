package be.timonc.customenchantments.enchantments.custom.triggers.movement;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.Set;

public class PlayerSneakUpTrigger extends TriggerListener {


    public PlayerSneakUpTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }

    @EventHandler
    public void onPlayerSneakUp(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) return;

        triggerInvoker.trigger(event, event.getPlayer());
    }


    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of();
    }
}
