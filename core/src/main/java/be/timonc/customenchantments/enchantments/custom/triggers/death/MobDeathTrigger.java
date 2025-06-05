package be.timonc.customenchantments.enchantments.custom.triggers.death;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MobDeathTrigger extends TriggerListener {

    private final TriggerConditionGroup deadMobConditions = new TriggerConditionGroup(
            "dead", TriggerConditionGroupType.ENTITY
    );
    private final TriggerConditionGroup lastDamageConditions = new TriggerConditionGroup(
            "last_damage", TriggerConditionGroupType.CAUSE
    );

    public MobDeathTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onAnimalDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Mob)) return;

        Bukkit.getOnlinePlayers().forEach(player -> triggerInvoker.trigger(
                event, player, Map.of(
                        deadMobConditions, event.getEntity(),
                        lastDamageConditions, Objects.requireNonNull(event.getEntity().getLastDamageCause()).getCause()
                )
        ));
    }


    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(deadMobConditions, lastDamageConditions);
    }
}
