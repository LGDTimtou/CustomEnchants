package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

public class BreakBlockTrigger extends Trigger {
    public BreakBlockTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Location loc = e.getBlock().getLocation();
        executeCommands(e, e.getPlayer(), e.getBlock().getType().name(), Map.of("block_x", String.valueOf(loc.getX()), "block_y", String.valueOf(loc.getY()), "block_z", String.valueOf(loc.getZ()), "block", e.getBlock().getType().name()));
    }



}
