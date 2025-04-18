package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block_other;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.TNTPrimeEvent;

import java.util.Map;

public class PrimeTNTTrigger extends Trigger {
    public PrimeTNTTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onPrimeTNT(TNTPrimeEvent e) {
        if (!(e.getPrimingEntity() instanceof Player player)) return;

        executeCommands(
                e,
                player,
                Map.of(new ConditionKey(TriggerConditionType.CAUSE, "prime"), e.getCause()),
                Map.of()
        );
    }
}
