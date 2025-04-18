package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;

import java.util.Map;

public class BlockDamagedTrigger extends Trigger {
    public BlockDamagedTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent e) {
        executeCommands(
                e,
                e.getPlayer(),
                Map.of(new ConditionKey(TriggerConditionType.BLOCK, ""), e.getBlock()),
                Map.of()
        );
    }
}
