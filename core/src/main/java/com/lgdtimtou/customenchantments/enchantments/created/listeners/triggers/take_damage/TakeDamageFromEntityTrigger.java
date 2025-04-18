package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.take_damage;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;

public class TakeDamageFromEntityTrigger extends Trigger {
    public TakeDamageFromEntityTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onDamageFromEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;

        Entity entity = e.getDamager();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        e.getDamager().addScoreboardTag(uniqueTag);

        executeCommands(
                e,
                player,
                Map.of(new ConditionKey(TriggerConditionType.ENTITY, "damager"), entity),
                Map.of("entity_tag", () -> uniqueTag),
                () -> entity.removeScoreboardTag(uniqueTag)
        );
    }
}
