package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.click;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.Set;

public class RightClickItemTrigger extends Trigger {
    public RightClickItemTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }


    @EventHandler
    public void onLeftClickItem(PlayerInteractEvent e){
        if (e.getItem() == null)
            return;
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
            return;

        executeCommands(e, e.getPlayer(), Set.of(), e.getItem().getType().name(), Map.of(
                "item", e.getItem().getType().name()
        ));
    }

}
