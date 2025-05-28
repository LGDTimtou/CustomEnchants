package be.timonc.customenchantments.enchantments.created.triggers.fishing_rod;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;
import java.util.Set;

public class FishingRodHitPlayerTrigger extends TriggerListener {

    private final TriggerConditionGroup hitPlayerConditions = new TriggerConditionGroup(
            "hit", TriggerConditionGroupType.PLAYER
    );

    public FishingRodHitPlayerTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onPlayerHit(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.CAUGHT_ENTITY) return;
        if (!(e.getCaught() instanceof Player caught)) return;

        triggerInvoker.trigger(
                e,
                e.getPlayer(),
                Map.of(hitPlayerConditions, caught)
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(hitPlayerConditions);
    }
}
