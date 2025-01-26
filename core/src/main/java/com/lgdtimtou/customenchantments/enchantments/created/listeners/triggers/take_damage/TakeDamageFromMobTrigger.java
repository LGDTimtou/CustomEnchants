package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.take_damage;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;

public class TakeDamageFromMobTrigger extends Trigger {
    public TakeDamageFromMobTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onTakeDamageFromMob(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;
        if (!(e.getDamager() instanceof Monster monster))
            return;


        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        monster.addScoreboardTag(uniqueTag);
        executeCommands(e, player, monster.getType().name(), Map.of(
                "mob", monster.getType().name(),
                "entity_tag", uniqueTag
        ), () -> monster.removeScoreboardTag(uniqueTag));
    }
}
