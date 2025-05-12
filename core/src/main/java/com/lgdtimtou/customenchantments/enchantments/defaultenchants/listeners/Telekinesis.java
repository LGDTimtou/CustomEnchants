package com.lgdtimtou.customenchantments.enchantments.defaultenchants.listeners;

import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import com.lgdtimtou.customenchantments.enchantments.defaultenchants.DefaultCustomEnchant;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashSet;
import java.util.Set;

public class Telekinesis implements CustomEnchantListener {


    @EventHandler
    public void blockBreak(BlockDropItemEvent e) {
        Player player = e.getPlayer();
        DefaultCustomEnchant defaultCustomEnchant = DefaultCustomEnchant.TELEKINESIS;

        if (!defaultCustomEnchant.check(player)) return;

        PlayerInventory inv = player.getInventory();
        if (Util.getEnchantedItem(player, defaultCustomEnchant.get()) == null)
            return;

        e.setCancelled(true);
        for (Item item : e.getItems()) {
            ItemStack leftover = inv.addItem(item.getItemStack()).get(0);
            if (leftover != null) {
                Location location = e.getBlock().getLocation();
                location.getWorld().dropItemNaturally(location, leftover);
            }
        }
    }

    @EventHandler
    public void killEnemy(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null)
            return;

        PlayerInventory inv = killer.getInventory();
        if (Util.getEnchantedItem(killer, DefaultCustomEnchant.TELEKINESIS.get()) == null)
            return;

        Set<ItemStack> set = new HashSet<>();
        e.getDrops().forEach(item -> set.add(inv.addItem(item).get(0)));
        e.getDrops().clear();
        set.forEach(e.getDrops()::add);
    }
}
