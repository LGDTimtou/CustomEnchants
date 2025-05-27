package be.timonc.customenchantments.enchantments.created.triggers.fishing_rod;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;

public class FishingRodHitPlayerTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public FishingRodHitPlayerTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onPlayerHit(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.CAUGHT_ENTITY) return;
        if (!(e.getCaught() instanceof Player caught)) return;

        triggerInvoker.trigger(
                e,
                e.getPlayer(),
                Map.of(new ConditionKey(TriggerConditionType.PLAYER, "caught"), caught),
                Map.of()
        );
    }
}
