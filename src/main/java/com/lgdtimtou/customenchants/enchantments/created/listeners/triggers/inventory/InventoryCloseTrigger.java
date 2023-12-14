package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.inventory;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;

public class InventoryCloseTrigger extends Trigger {
    public InventoryCloseTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        executeCommands(e, (Player) e.getPlayer(), e.getInventory().getType().name(), Map.of("type", e.getInventory().getType().name()));
    }
}
