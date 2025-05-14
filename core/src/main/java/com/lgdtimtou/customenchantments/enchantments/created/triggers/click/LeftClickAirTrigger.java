package com.lgdtimtou.customenchantments.enchantments.created.triggers.click;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class LeftClickAirTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public LeftClickAirTrigger(TriggerType type) {
        this.triggerType = type;
    }


    @EventHandler
    public void onPlayerLeftClickAir(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR) return;

        triggerType.trigger(event, event.getPlayer(), Map.of(), Map.of());
    }
}
