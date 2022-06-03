package com.lgdtimtou.customenchants.enchantments.listeners;

import com.lgdtimtou.customenchants.Util;
import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;

public class HeadHunter implements CustomEnchantListener {

    private static final Random RG = new Random();
    private static final int[] CHANCES = new int[]{25, 50, 100};


    @EventHandler
    public void playerDeath(PlayerDeathEvent e){
        Player killer = e.getEntity().getKiller();
        if (killer == null)
            return;

        PlayerInventory inv = killer.getInventory();
        if (inv.getItemInMainHand().getItemMeta() == null)
            return;
        if (!inv.getItemInMainHand().containsEnchantment(CustomEnchant.HEAD_HUNTER.getEnchantment()))
            return;

        int level = inv.getItemInMainHand().getEnchantmentLevel(CustomEnchant.HEAD_HUNTER.getEnchantment());
        int chance = CHANCES[level - 1];

        if (RG.nextInt(101) <= chance){
            ItemStack head = Util.getPlayerHead(e.getEntity().getUniqueId());
            ItemStack leftover = killer.getInventory().addItem(head).get(0);
            if (leftover != null)
                killer.getWorld().dropItemNaturally(e.getEntity().getLocation(), head);
        }
    }
}
