package com.lgdtimtou.customenchantments.enchantments.created.triggers.block;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockIgniteEvent;

import java.util.Map;

public class BlockIgniteTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public BlockIgniteTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        if (e.getPlayer() == null) return;

        triggerType.trigger(e, e.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.BLOCK, ""),
                e.getBlock(),
                new ConditionKey(TriggerConditionType.CAUSE, "ignite"),
                e.getCause()
        ), Map.of());
    }
}

