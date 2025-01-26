package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.projectiles;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Map;
import java.util.UUID;

public class ProjectileLandTrigger extends Trigger {
    private final EntityType projectile;

    public ProjectileLandTrigger(CustomEnchant customEnchant, EnchantTriggerType type){
        super(customEnchant, type);
        this.projectile = null;
    }

    public ProjectileLandTrigger(CustomEnchant customEnchant, EnchantTriggerType type, EntityType projectile) {
        super(customEnchant, type);
        this.projectile = projectile;
    }

    @EventHandler
    public void onProjectileHitEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Projectile usedProjectile))
            return;

        handleTrigger(e, usedProjectile);
    }

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent e){
        handleTrigger(e, e.getEntity());
    }

    private void handleTrigger(Event e, Projectile usedProjectile) {
        if (projectile != null && usedProjectile.getType() != projectile)
            return;
        if (!(usedProjectile.getShooter() instanceof Player player))
            return;


        String projectileUniqueTag = "projectile_" + UUID.randomUUID().toString().substring(0, 8);
        usedProjectile.addScoreboardTag(projectileUniqueTag);

        Location projectileLoc = usedProjectile.getLocation();
        executeCommands(e, player, null, Map.of(
                "projectile_x", String.valueOf(projectileLoc.getX()),
                "projectile_y", String.valueOf(projectileLoc.getY()),
                "projectile_z", String.valueOf(projectileLoc.getZ()),
                "projectile_tag", projectileUniqueTag
        ), () -> usedProjectile.removeScoreboardTag(projectileUniqueTag));
    }
}