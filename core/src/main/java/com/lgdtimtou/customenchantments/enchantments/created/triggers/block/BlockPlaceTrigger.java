package com.lgdtimtou.customenchantments.enchantments.created.triggers.block;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;

public class BlockPlaceTrigger implements CustomEnchantListener {

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
