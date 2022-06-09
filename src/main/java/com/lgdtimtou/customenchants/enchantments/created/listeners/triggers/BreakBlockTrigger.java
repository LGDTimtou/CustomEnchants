package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

public class BreakBlockTrigger extends Trigger {

    //https://www.spigotmc.org/threads/itemstack-to-json.394695/ get json of items
    //https://gist.github.com/DevSrSouza/aa2f39fb2299dfe72b49b52fa46d9a73

    /*
    Command variables:
     - %player% = player who broke the block
     -
     */

    public BreakBlockTrigger(Enchantment enchantment){
        super(enchantment);
    }




    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        executeCommands(e, e.getPlayer(), null, Map.of());
    }



}
