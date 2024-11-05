package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.fishing_rod;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;

public class FishingRodHitPlayerTrigger extends Trigger {
    public FishingRodHitPlayerTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
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
