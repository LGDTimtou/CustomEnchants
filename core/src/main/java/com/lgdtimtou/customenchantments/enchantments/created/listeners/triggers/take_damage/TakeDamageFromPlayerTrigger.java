package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.take_damage;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class TakeDamageFromPlayerTrigger extends Trigger {
    public TakeDamageFromPlayerTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onTakeDamageFromPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        if (!(e.getDamager() instanceof Player damager))
            return;
        executeCommands(e, player, Map.of(
                new ConditionKey(TriggerConditionType.PLAYER, "damager"), damager,
                new ConditionKey(TriggerConditionType.CAUSE, "damage"), e.getCause()
        ), Map.of());
    }
}
