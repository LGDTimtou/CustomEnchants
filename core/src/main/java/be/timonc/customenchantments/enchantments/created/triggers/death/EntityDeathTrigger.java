package be.timonc.customenchantments.enchantments.created.triggers.death;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class EntityDeathTrigger extends TriggerListener {

    private final TriggerConditionGroup deadEntityConditions = new TriggerConditionGroup(
            "dead", TriggerConditionGroupType.ENTITY
    );
    private final TriggerConditionGroup lastDamageConditions = new TriggerConditionGroup(
            "last_damage", TriggerConditionGroupType.CAUSE
    );

    public EntityDeathTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Bukkit.getOnlinePlayers().forEach(player -> triggerInvoker.trigger(event, player, Map.of(
                deadEntityConditions, event.getEntity(),
                lastDamageConditions, Objects.requireNonNull(event.getEntity().getLastDamageCause()).getCause()
        )));
    }


    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(deadEntityConditions, lastDamageConditions);
    }
}
