package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.damage;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class DamagePlayerTrigger extends Trigger {

    public DamagePlayerTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player damaged)) return;
        if (!(e.getDamager() instanceof Player player)) return;

        double health = damaged.getHealth();
        double damage = e.getDamage();

        executeCommands(e, player, Map.of(
                new ConditionKey(TriggerConditionType.PLAYER, "damaged"), damaged,
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "damaged_health"), health,
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "damaged_health"), health,
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "damaged_health"), health,
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "damage"), damage,
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "damage"), damage,
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "damage"), damage
        ), Map.of());
    }
}
