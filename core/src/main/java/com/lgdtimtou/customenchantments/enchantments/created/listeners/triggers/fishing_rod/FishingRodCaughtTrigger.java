package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.fishing_rod;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class FishingRodCaughtTrigger extends Trigger {
    public FishingRodCaughtTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onFish(PlayerFishEvent e){
        if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;
        if (e.getCaught() == null)
            return;
        executeCommands(e, e.getPlayer(), ((ItemStack)e.getCaught()).getType().name(), Map.of("item", ((ItemStack)e.getCaught()).getType().name()));
    }
}
