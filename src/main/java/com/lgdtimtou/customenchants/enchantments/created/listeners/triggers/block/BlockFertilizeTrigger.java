package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchants.other.Util;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFertilizeEvent;

import java.util.Map;

public class BlockFertilizeTrigger extends Trigger {
    public BlockFertilizeTrigger(Enchantment enchantment) {
        super(enchantment);
    }


    @EventHandler
    public void onFertilize(BlockFertilizeEvent e){
        executeCommands(e, e.getPlayer(), e.getBlock().getType().name(), Map.of("material", e.getBlock().getType().name()));
    }

}