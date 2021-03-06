package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;

public class FishingRodCaughtTrigger extends Trigger{
    public FishingRodCaughtTrigger(Enchantment enchantment) {
        super(enchantment);
    }

    @EventHandler
    public void onFish(PlayerFishEvent e){
        if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;
        executeCommands(e, e.getPlayer(), null, Map.of());
    }
}
