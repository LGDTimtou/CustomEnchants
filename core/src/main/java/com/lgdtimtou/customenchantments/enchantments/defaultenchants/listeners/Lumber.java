package com.lgdtimtou.customenchantments.enchantments.defaultenchants.listeners;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchantments.enchantments.defaultenchants.DefaultCustomEnchant;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Lumber implements CustomEnchantListener {

    @EventHandler
    public void logBreakEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if (!isLog(e.getBlock().getType()))
            return;

        ItemStack enchantedItem = Util.getEnchantedItem(player.getInventory(), DefaultCustomEnchant.LUMBER.get());
        if (enchantedItem == null)
            return;

        breakTreeUpwards(e.getBlock().getLocation(), enchantedItem);
    }

    private void breakTreeUpwards(Location location, ItemStack tool) {
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Location newLocation = location.clone().add(x, y, z);
                    if (isLog(newLocation.getBlock().getType())) {
                        newLocation.getBlock().breakNaturally(tool);
                        breakTreeUpwards(newLocation, tool);
                    }
                }
            }
        }
    }

    private boolean isLog(Material material) {
        return material.name().endsWith("_LOG");
    }


}
