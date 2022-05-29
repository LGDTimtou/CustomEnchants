package com.lgdtimtou.replenish.enchantments;


import com.lgdtimtou.replenish.CustomEnchant;
import com.lgdtimtou.replenish.Main;
import com.lgdtimtou.replenish.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Replenish implements CustomEnchantListener {

    private static final Map<Material, Material> crops = Map.of(
            Material.WHEAT, Material.WHEAT_SEEDS,
            Material.BEETROOTS, Material.BEETROOT_SEEDS,
            Material.CARROTS, Material.CARROT,
            Material.POTATOES, Material.POTATO,
            Material.SUGAR_CANE, Material.SUGAR_CANE,
            Material.NETHER_WART, Material.NETHER_WART

    );



    @EventHandler
    public void blockBreak(BlockBreakEvent e){
        if (!e.getPlayer().getInventory().getItemInMainHand().containsEnchantment(CustomEnchant.REPLENISH.getEnchantment()))
            return;


        Inventory inv = e.getPlayer().getInventory();
        Block b = e.getBlock();
        Material crop = b.getType();
        Material seed = crops.get(crop);
        if (seed == null)
            return;
        if (!inv.contains(seed))
            return;

        //Removing seed from inventory
        if (e.getPlayer().getGameMode() == GameMode.SURVIVAL){
            for (int i = 0; i < inv.getSize(); i++) {
                ItemStack item = inv.getItem(i);
                if (item != null && item.getType() == seed){
                    item.setAmount(item.getAmount() - 1);
                    e.getPlayer().getInventory().setItem(i, item);
                    break;
                }
            }
        }

        Bukkit.getScheduler().runTaskLater(Main.getMain(), () -> b.setType(crop), 2L);
    }


}
