package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block_other;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchants.other.Util;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockReceiveGameEvent;

import java.util.Map;

public class ActivateSculkSensorTrigger extends Trigger {
    public ActivateSculkSensorTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
    }

    @EventHandler
    public void onActivateSculkSensor(BlockReceiveGameEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;

        executeCommands(e, player, null, Map.of());
    }
}
