package com.lgdtimtou.customenchantments.enchantments.created.triggers.click;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class RightClickAirTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public RightClickAirTrigger(TriggerType type) {
        this.triggerType = type;
    }


    @EventHandler
    public void onPlayerRightClickAir(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        triggerType.trigger(event, event.getPlayer(), Map.of(), Map.of());
    }
}
