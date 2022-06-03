package com.lgdtimtou.customenchants.enchantments.listeners;

import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class Telekinesis implements CustomEnchantListener {

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        PlayerInventory inv = e.getPlayer().getInventory();
        if (inv.getItemInMainHand().getItemMeta() == null)
            return;
        if (!inv.getItemInMainHand().containsEnchantment(CustomEnchant.TELEKINESIS.getEnchantment()))
            return;
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL)
            return;

        e.setDropItems(false);
        for (ItemStack item : e.getBlock().getDrops()){
            ItemStack leftover = inv.addItem(item).get(0);
            if (leftover != null){
                Location location = e.getBlock().getLocation();
                location.getWorld().dropItemNaturally(location, leftover);
            }
        }
    }

    @EventHandler
    public void killEnemy(EntityDeathEvent e){
        Player killer = e.getEntity().getKiller();
        if (killer == null)
            return;

        PlayerInventory inv = killer.getInventory();
        if (inv.getItemInMainHand().getItemMeta() == null)
            return;
        if (!inv.getItemInMainHand().containsEnchantment(CustomEnchant.TELEKINESIS.getEnchantment()))
            return;
        if (killer.getGameMode() != GameMode.SURVIVAL)
            return;


        Set<ItemStack> set = new HashSet<>();
        e.getDrops().forEach(item -> set.add(inv.addItem(item).get(0)));
        e.getDrops().clear();
        set.forEach(e.getDrops()::add);
    }
}
