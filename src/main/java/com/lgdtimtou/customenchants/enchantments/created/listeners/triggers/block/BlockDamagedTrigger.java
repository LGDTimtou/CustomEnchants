package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;

import java.util.Map;

public class BlockDamagedTrigger extends Trigger {
    public BlockDamagedTrigger(Enchantment enchantment) {
        super(enchantment);
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent e){
        Location loc = e.getBlock().getLocation();
        executeCommands(e, e.getPlayer(), e.getBlock().getType().name(), Map.of("block_x", String.valueOf(loc.getX()), "block_y", String.valueOf(loc.getY()), "block_z", String.valueOf(loc.getZ()), "block", e.getBlock().getType().name()));
    }


}
