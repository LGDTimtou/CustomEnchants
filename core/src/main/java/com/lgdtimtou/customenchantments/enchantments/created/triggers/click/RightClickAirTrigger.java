package com.lgdtimtou.customenchantments.enchantments.created.triggers.click;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class RightClickAirTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public RightClickAirTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }


    @EventHandler
    public void onPlayerRightClickAir(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        triggerInvoker.trigger(event, event.getPlayer(), Map.of(), Map.of());
    }
}
