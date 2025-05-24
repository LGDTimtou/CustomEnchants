package be.timonc.customenchantments.enchantments.created.triggers.fishing_rod;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;

public class FishingRodCaughtTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public FishingRodCaughtTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (e.getCaught() == null) return;

        triggerInvoker.trigger(
                e,
                e.getPlayer(),
                Map.of(new ConditionKey(TriggerConditionType.ITEM, "caught"), ((Item) e.getCaught()).getItemStack()),
                Map.of()
        );
    }
}
