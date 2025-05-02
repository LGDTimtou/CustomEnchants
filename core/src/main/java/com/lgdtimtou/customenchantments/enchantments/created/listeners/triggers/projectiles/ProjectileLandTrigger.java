package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.projectiles;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Map;
import java.util.UUID;

public class ProjectileLandTrigger extends Trigger {

    public ProjectileLandTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }


    @EventHandler
    public void onProjectileHitEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Projectile usedProjectile))
            return;

        handleTrigger(e, usedProjectile);
    }

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent e) {
        handleTrigger(e, e.getEntity());
    }

    private void handleTrigger(Event e, Projectile usedProjectile) {
        if (!(usedProjectile.getShooter() instanceof Player player))
            return;

        String projectileUniqueTag = "projectile_" + UUID.randomUUID().toString().substring(0, 8);
        usedProjectile.addScoreboardTag(projectileUniqueTag);

        executeCommands(e, player, Map.of(
                new ConditionKey(TriggerConditionType.ENTITY, "projectile"), usedProjectile
        ), Map.of(
                "projectile_tag", () -> projectileUniqueTag
        ), () -> usedProjectile.removeScoreboardTag(projectileUniqueTag));
    }
}