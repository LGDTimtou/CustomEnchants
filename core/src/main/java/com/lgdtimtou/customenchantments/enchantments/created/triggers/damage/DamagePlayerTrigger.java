package com.lgdtimtou.customenchantments.enchantments.created.triggers.damage;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class DamagePlayerTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public DamagePlayerTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player damaged)) return;
        if (!(e.getDamager() instanceof Player player)) return;

        double health = damaged.getHealth();
        double damage = e.getDamage();

        triggerType.trigger(e, player, Map.of(
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
