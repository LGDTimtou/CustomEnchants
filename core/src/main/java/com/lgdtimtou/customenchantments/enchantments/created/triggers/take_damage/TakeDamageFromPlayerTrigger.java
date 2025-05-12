package com.lgdtimtou.customenchantments.enchantments.created.triggers.take_damage;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class TakeDamageFromPlayerTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public TakeDamageFromPlayerTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onTakeDamageFromPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        if (!(e.getDamager() instanceof Player damager))
            return;
        triggerType.trigger(e, player, Map.of(
                new ConditionKey(TriggerConditionType.PLAYER, "damager"), damager,
                new ConditionKey(TriggerConditionType.CAUSE, "damage"), e.getCause()
        ), Map.of());
    }
}
