package be.timonc.customenchantments.enchantments.created.triggers.death;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AnimalDeathTrigger extends TriggerListener {

    private final TriggerConditionGroup deadAnimalConditions = new TriggerConditionGroup(
            "dead", TriggerConditionGroupType.ENTITY
    );
    private final TriggerConditionGroup lastDamageConditions = new TriggerConditionGroup(
            "last_damage", TriggerConditionGroupType.CAUSE
    );

    public AnimalDeathTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onAnimalDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Animals)) return;

        Bukkit.getOnlinePlayers().forEach(player -> triggerInvoker.trigger(event, player, Map.of(
                deadAnimalConditions, event.getEntity(),
                lastDamageConditions, Objects.requireNonNull(event.getEntity().getLastDamageCause()).getCause()
        )));
    }


    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(deadAnimalConditions, lastDamageConditions);
    }
}
