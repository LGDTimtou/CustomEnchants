package com.lgdtimtou.customenchantments.enchantments.created.triggers.fishing_rod;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;

public class FishingRodHitPlayerTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public FishingRodHitPlayerTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onPlayerHit(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.CAUGHT_ENTITY) return;
        if (!(e.getCaught() instanceof Player caught)) return;

        triggerType.trigger(
                e,
                e.getPlayer(),
                Map.of(new ConditionKey(TriggerConditionType.PLAYER, "caught"), caught),
                Map.of()
        );
    }
}
