package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.take_damage;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
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
    public void onDamageFromEntity(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;

        Entity entity = e.getDamager();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        e.getDamager().addScoreboardTag(uniqueTag);
        executeCommands(e, player, e.getDamager().getType().name(), Map.of(
                "entity", e.getDamager().getType().name(),
                "entity_tag", uniqueTag
        ), () -> entity.removeScoreboardTag(uniqueTag));
    }
}
