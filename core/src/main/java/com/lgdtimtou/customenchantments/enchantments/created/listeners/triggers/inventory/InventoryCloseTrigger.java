package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.inventory;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;

public class InventoryCloseTrigger extends Trigger {
    public InventoryCloseTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        executeCommands(e, (Player) e.getPlayer(), e.getInventory().getType().name(), Map.of("type", e.getInventory().getType().name()));
    }
}
