package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block_other;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BellRingEvent;

import java.util.Map;

public class BellRungTrigger extends Trigger {
    public BellRungTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
    }

    @EventHandler
    public void onBellRing(BellRingEvent e){
        if (e.getEntity() == null || !(e.getEntity() instanceof Player player))
            return;
        executeCommands(e, player, null, Map.of());
    }
}
