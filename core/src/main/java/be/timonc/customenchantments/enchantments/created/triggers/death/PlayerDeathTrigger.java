package be.timonc.customenchantments.enchantments.created.triggers.death;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;
import java.util.Set;

public class PlayerDeathTrigger extends TriggerListener {

    private final TriggerConditionGroup deadPlayerConditions = new TriggerConditionGroup(
            "dead", TriggerConditionGroupType.PLAYER
    );


    protected PlayerDeathTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Bukkit.getOnlinePlayers().forEach(player -> triggerInvoker.trigger(event, player, Map.of(
                deadPlayerConditions, event.getEntity()
        )));
    }


    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of();
    }
}
