package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.Location;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;

public class BlockPlaceTrigger extends Trigger {


    public BlockPlaceTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e){
        if (!e.canBuild())
            return;
        Location loc = e.getBlock().getLocation();
        executeCommands(e, e.getPlayer(), e.getBlockPlaced().getType().name(), Map.of("block_x", String.valueOf(loc.getX()), "block_y", String.valueOf(loc.getY()), "block_z", String.valueOf(loc.getZ()), "block", e.getBlock().getType().name()));
    }


}
