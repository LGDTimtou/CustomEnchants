package be.timonc.customenchantments.enchantments.custom.triggers.fishing_rod;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FishingRodHitEntityTrigger extends TriggerListener {

    private final TriggerConditionGroup hitEntityConditions = new TriggerConditionGroup(
            "hit", TriggerConditionGroupType.ENTITY
    );

    public FishingRodHitEntityTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onPlayerHit(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.CAUGHT_ENTITY) return;
        if (e.getCaught() == null) return;

        Entity entity = e.getCaught();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        entity.addScoreboardTag(uniqueTag);

        triggerInvoker.trigger(
                e,
                e.getPlayer(),
                Map.of(hitEntityConditions, entity),
                Map.of("hit_tag", () -> uniqueTag),
                () -> entity.removeScoreboardTag(uniqueTag)
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(hitEntityConditions);
    }
}
