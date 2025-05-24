package com.lgdtimtou.customenchantments.enchantments.created.triggers.inventory;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;

public class InventoryCloseTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public InventoryCloseTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        triggerInvoker.trigger(e, (Player) e.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.INVENTORY, "top"), e.getView().getTopInventory(),
                new ConditionKey(TriggerConditionType.INVENTORY, "bottom"), e.getView().getBottomInventory(),
                new ConditionKey(TriggerConditionType.STRING, "title"), e.getView().getTitle()
        ), Map.of());
    }
}
