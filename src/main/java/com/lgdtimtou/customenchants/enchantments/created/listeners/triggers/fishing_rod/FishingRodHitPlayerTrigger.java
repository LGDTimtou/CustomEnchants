package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.fishing_rod;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;

public class FishingRodHitPlayerTrigger extends Trigger {
    public FishingRodHitPlayerTrigger(Enchantment enchantment){
        super(enchantment);
    }

    @EventHandler
    public void onPlayerHit(PlayerFishEvent e){
        if (e.getState() != PlayerFishEvent.State.CAUGHT_ENTITY)
            return;
        if (!(e.getCaught() instanceof Player caught))
            return;
        executeCommands(e, e.getPlayer(), caught.getDisplayName(), Map.of("damaged", caught.getDisplayName()));
    }
}
