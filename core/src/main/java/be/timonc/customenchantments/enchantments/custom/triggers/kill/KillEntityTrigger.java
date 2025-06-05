package be.timonc.customenchantments.enchantments.custom.triggers.kill;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class KillEntityTrigger extends TriggerListener {

    private final TriggerConditionGroup killedEntityConditions = new TriggerConditionGroup(
            "killed", TriggerConditionGroupType.ENTITY
    );

    public KillEntityTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player killer)) return;
        Entity entity = e.getEntity();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        entity.addScoreboardTag(uniqueTag);
        triggerInvoker.trigger(
                e,
                killer,
                Map.of(killedEntityConditions, entity),
                Map.of("entity_tag", () -> uniqueTag),
                () -> entity.removeScoreboardTag(uniqueTag)
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(killedEntityConditions);
    }
}
