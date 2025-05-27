package be.timonc.customenchantments.enchantments.created.triggers.fishing_rod;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;
import java.util.UUID;

public class FishingRodHitEntityTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public FishingRodHitEntityTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
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
                Map.of(new ConditionKey(TriggerConditionType.ENTITY, "hit"), entity),
                Map.of("hit_tag", () -> uniqueTag),
                () -> entity.removeScoreboardTag(uniqueTag)
        );
    }
}
