package com.lgdtimtou.customenchantments.enchantments.created.triggers.block_other;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockReceiveGameEvent;

import java.util.Map;

public class ActivateSculkSensorTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public ActivateSculkSensorTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onActivateSculkSensor(BlockReceiveGameEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        triggerType.trigger(e, player, Map.of(), Map.of());
    }
}
