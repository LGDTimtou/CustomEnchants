package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.fishing_rod;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;

public class FishingRodCaughtTrigger extends Trigger {
    public FishingRodCaughtTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (e.getCaught() == null) return;

        executeCommands(
                e,
                e.getPlayer(),
                Map.of(new ConditionKey(TriggerConditionType.ITEM, "caught"), ((Item) e.getCaught()).getItemStack()),
                Map.of()
        );
    }
}
