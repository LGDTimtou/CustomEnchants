package be.timonc.customenchantments.enchantments.custom.triggers.damage;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DamageEntityTrigger extends TriggerListener {

    private final TriggerConditionGroup damagedEntityConditions = new TriggerConditionGroup(
            "damaged", TriggerConditionGroupType.ENTITY
    );
    private final TriggerConditionGroup damageConditions = new TriggerConditionGroup(
            "damage", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup damageCauseConditions = new TriggerConditionGroup(
            "damage", TriggerConditionGroupType.CAUSE
    );

    public DamageEntityTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;

        Entity entity = e.getEntity();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        entity.addScoreboardTag(uniqueTag);

        triggerInvoker.trigger(
                e,
                player,
                Map.of(
                        damagedEntityConditions, entity,
                        damageConditions, e.getDamage(),
                        damageCauseConditions, e.getCause()
                ),
                Map.of("entity_tag", () -> uniqueTag),
                () -> entity.removeScoreboardTag(uniqueTag)
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(damagedEntityConditions, damageConditions, damageCauseConditions);
    }
}

