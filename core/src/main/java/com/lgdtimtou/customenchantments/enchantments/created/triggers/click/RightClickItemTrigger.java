package com.lgdtimtou.customenchantments.enchantments.created.triggers.click;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class RightClickItemTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public RightClickItemTrigger(TriggerType type) {
        this.triggerType = type;
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        triggerType.trigger(e, e.getPlayer(), Map.of(), Map.of());
    }
}

