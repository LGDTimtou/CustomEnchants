package be.timonc.customenchantments.enchantments.custom.triggers.inventory;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Map;
import java.util.Set;

public class InventoryOpenTrigger extends TriggerListener {

    private final TriggerConditionGroup topInventoryConditions = new TriggerConditionGroup(
            "top", TriggerConditionGroupType.INVENTORY
    );
    private final TriggerConditionGroup bottomInventoryConditions = new TriggerConditionGroup(
            "bottom", TriggerConditionGroupType.INVENTORY
    );
    private final TriggerConditionGroup titleConditions = new TriggerConditionGroup(
            "inventory_title", TriggerConditionGroupType.STRING
    );

    public InventoryOpenTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        triggerInvoker.trigger(
                e, (Player) e.getPlayer(), Map.of(
                        topInventoryConditions, e.getView().getTopInventory(),
                        bottomInventoryConditions, e.getView().getBottomInventory(),
                        titleConditions, e.getView().getTitle()
                )
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(topInventoryConditions, bottomInventoryConditions, titleConditions);
    }
}
