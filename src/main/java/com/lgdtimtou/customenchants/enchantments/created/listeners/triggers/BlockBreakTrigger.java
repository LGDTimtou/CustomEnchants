package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlockBreakTrigger extends Trigger {

    //https://www.spigotmc.org/threads/itemstack-to-json.394695/ get json of items
    //https://gist.github.com/DevSrSouza/aa2f39fb2299dfe72b49b52fa46d9a73

    /*
    Command variables:
     - %player% = player who broke the block
     -
     */

    public BlockBreakTrigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels){
        super(enchantment, levels);
    }




    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if (!defaultChecks(e.getPlayer()))
            return;

        e.setCancelled(this.isCancelled());
        getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }



}
