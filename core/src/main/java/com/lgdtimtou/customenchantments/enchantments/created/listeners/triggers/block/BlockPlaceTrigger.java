package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;

public class BlockPlaceTrigger extends Trigger {


    public BlockPlaceTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (!e.canBuild()) return;
        executeCommands(e, e.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.BLOCK, "placed"),
                e.getBlockPlaced(),
                new ConditionKey(TriggerConditionType.BLOCK, "against"),
                e.getBlockAgainst()
        ), Map.of());
    }
}
