package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.inventory;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;

public class InventoryCloseTrigger extends Trigger {
    public InventoryCloseTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        executeCommands(e, (Player) e.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.INVENTORY, "top"), e.getView().getTopInventory(),
                new ConditionKey(TriggerConditionType.INVENTORY, "bottom"), e.getView().getBottomInventory(),
                new ConditionKey(TriggerConditionType.STRING, "title"), e.getView().getTitle()
        ), Map.of());
    }
}
