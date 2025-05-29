package be.timonc.customenchantments.enchantments.created.triggers.health_change;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TakeDamageFromEntityTrigger extends TriggerListener {

    private final TriggerConditionGroup attackerEntityConditions = new TriggerConditionGroup(
            "attacker", TriggerConditionGroupType.ENTITY
    );
    private final TriggerConditionGroup newHealthConditions = new TriggerConditionGroup(
            "new_health", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup previousHealthConditions = new TriggerConditionGroup(
            "previous_health", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup damageAmountConditions = new TriggerConditionGroup(
            "damage", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup damageCauseConditions = new TriggerConditionGroup(
            "damage", TriggerConditionGroupType.CAUSE
    );

    public TakeDamageFromEntityTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onDamageFromEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        Entity entity = event.getDamager();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        event.getDamager().addScoreboardTag(uniqueTag);

        triggerInvoker.trigger(event, player,
                Map.of(
                        attackerEntityConditions, entity,
                        newHealthConditions, player.getHealth() - event.getFinalDamage(),
                        previousHealthConditions, player.getHealth(),
                        damageAmountConditions, event.getFinalDamage(),
                        damageCauseConditions, event.getCause()
                ),
                Map.of("entity_tag", () -> uniqueTag),
                () -> entity.removeScoreboardTag(uniqueTag)
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(
                attackerEntityConditions,
                newHealthConditions,
                previousHealthConditions,
                damageAmountConditions,
                damageCauseConditions
        );
    }
}
