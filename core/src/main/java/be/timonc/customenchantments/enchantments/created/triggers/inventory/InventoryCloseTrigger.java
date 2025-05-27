package be.timonc.customenchantments.enchantments.created.triggers.inventory;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;

public class InventoryCloseTrigger implements TriggerListener {

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
