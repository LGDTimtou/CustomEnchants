package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block_other;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.TNTPrimeEvent;

import java.util.Map;

public class PrimeTNTTrigger extends Trigger {
    public PrimeTNTTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onPrimeTNT(TNTPrimeEvent e){
        if (!(e.getPrimingEntity() instanceof Player player))
            return;

        executeCommands(e, player, e.getCause().name(), Map.of("cause", e.getCause().name()));
    }
}
