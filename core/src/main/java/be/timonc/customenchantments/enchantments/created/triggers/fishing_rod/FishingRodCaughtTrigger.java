package be.timonc.customenchantments.enchantments.created.triggers.fishing_rod;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;
import java.util.Set;

public class FishingRodCaughtTrigger extends TriggerListener {

    private final TriggerConditionGroup caughtItemConditions = new TriggerConditionGroup(
            "caught", TriggerConditionGroupType.ITEM
    );

    public FishingRodCaughtTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (e.getCaught() == null) return;

        triggerInvoker.trigger(
                e,
                e.getPlayer(),
                Map.of(
                        caughtItemConditions,
                        ((Item) e.getCaught()).getItemStack()
                )
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(caughtItemConditions);
    }
}
