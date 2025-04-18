package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block_other;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BellRingEvent;

import java.util.Map;

public class BellRungTrigger extends Trigger {
    public BellRungTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onBellRing(BellRingEvent e) {
        if (e.getEntity() == null || !(e.getEntity() instanceof Player player)) return;
        executeCommands(e, player, Map.of(), Map.of());
    }
}
