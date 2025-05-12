package com.lgdtimtou.customenchantments.enchantments.created.triggers.click;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class LeftClickItemTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public LeftClickItemTrigger(TriggerType type) {
        this.triggerType = type;
    }


    @EventHandler
    public void onLeftClick(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)) return;

        triggerType.trigger(e, e.getPlayer(), Map.of(), Map.of());
    }
}

