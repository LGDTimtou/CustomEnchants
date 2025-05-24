package com.lgdtimtou.customenchantments.enchantments.created.triggers.click;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class LeftClickTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public LeftClickTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }


    @EventHandler
    public void onLeftClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK) return;

        triggerInvoker.trigger(e, e.getPlayer(), Map.of(), Map.of());
    }
}

