package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockIgniteEvent;

import java.util.Map;

public class BlockIgniteTrigger extends Trigger {
    public BlockIgniteTrigger(Enchantment enchantment) {
        super(enchantment);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e){
        if (e.getPlayer() == null)
            return;

        executeCommands(e, e.getPlayer(), e.getBlock().getType().name(), Map.of("block", e.getBlock().getType().name(), "cause", e.getCause().name()));
    }
}
