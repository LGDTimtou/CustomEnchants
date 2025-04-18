package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block_other;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockReceiveGameEvent;

import java.util.Map;

public class ActivateSculkSensorTrigger extends Trigger {
    public ActivateSculkSensorTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onActivateSculkSensor(BlockReceiveGameEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        executeCommands(e, player, Map.of(), Map.of());
    }
}
