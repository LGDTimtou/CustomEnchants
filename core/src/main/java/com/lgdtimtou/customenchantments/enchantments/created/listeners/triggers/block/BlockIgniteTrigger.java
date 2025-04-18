package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockIgniteEvent;

import java.util.Map;

public class BlockIgniteTrigger extends Trigger {
    public BlockIgniteTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        if (e.getPlayer() == null) return;

        executeCommands(e, e.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.BLOCK, ""),
                e.getBlock(),
                new ConditionKey(TriggerConditionType.CAUSE, "ignite"),
                e.getCause()
        ), Map.of());
    }
}

