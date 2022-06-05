package com.lgdtimtou.customenchants.enchantments.defaultenchants.listeners.listeners;


import com.lgdtimtou.customenchants.Main;
import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchants.enchantments.defaultenchants.DefaultCustomEnchant;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
        if (!e.getPlayer().getInventory().getItemInMainHand().containsEnchantment(DefaultCustomEnchant.REPLENISH.getEnchantment()))
            return;


        Inventory inv = e.getPlayer().getInventory();
        Block b = e.getBlock();
        Material crop = b.getType();
        Material seed = crops.get(crop);
        if (seed == null)
            return;
        if (!inv.contains(seed))
            return;
        Material relative = b.getRelative(BlockFace.DOWN).getType();
        if (relative != Material.FARMLAND && relative != Material.SAND && relative != Material.SOUL_SAND)
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
