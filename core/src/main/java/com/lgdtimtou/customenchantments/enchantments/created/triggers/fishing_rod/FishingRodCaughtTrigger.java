package com.lgdtimtou.customenchantments.enchantments.created.triggers.fishing_rod;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;

public class FishingRodCaughtTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public FishingRodCaughtTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (e.getCaught() == null) return;

        triggerType.trigger(
                e,
                e.getPlayer(),
                Map.of(new ConditionKey(TriggerConditionType.ITEM, "caught"), ((Item) e.getCaught()).getItemStack()),
                Map.of()
        );
    }
}
