package be.timonc.customenchantments.enchantments.created.triggers.block;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;

public class BlockPlaceTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public BlockPlaceTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (!e.canBuild()) return;
        triggerInvoker.trigger(e, e.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.BLOCK, "placed"),
                e.getBlockPlaced(),
                new ConditionKey(TriggerConditionType.BLOCK, "against"),
                e.getBlockAgainst()
        ), Map.of());
    }
}
