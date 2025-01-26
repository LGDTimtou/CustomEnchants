package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.fishing_rod;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

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

        Entity caught = e.getCaught();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        caught.addScoreboardTag(uniqueTag);
        executeCommands(e, e.getPlayer(), (e.getCaught()).getType().name(), Map.of(
                "caught", (e.getCaught()).getType().name(),
                "entity_tag", uniqueTag,
                "entity_x", String.valueOf(e.getCaught().getLocation().getX()),
                "entity_y", String.valueOf(e.getCaught().getLocation().getY()),
                "entity_z", String.valueOf(e.getCaught().getLocation().getZ())
        ), () -> caught.removeScoreboardTag(uniqueTag));
    }
}
