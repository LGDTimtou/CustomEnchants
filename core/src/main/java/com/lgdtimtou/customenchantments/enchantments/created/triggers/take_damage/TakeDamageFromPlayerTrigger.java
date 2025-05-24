package com.lgdtimtou.customenchantments.enchantments.created.triggers.take_damage;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class TakeDamageFromPlayerTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public TakeDamageFromPlayerTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onTakeDamageFromPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        if (!(e.getDamager() instanceof Player damager))
            return;
        triggerInvoker.trigger(e, player, Map.of(
                new ConditionKey(TriggerConditionType.PLAYER, "damager"), damager,
                new ConditionKey(TriggerConditionType.CAUSE, "damage"), e.getCause()
        ), Map.of());
    }
}
