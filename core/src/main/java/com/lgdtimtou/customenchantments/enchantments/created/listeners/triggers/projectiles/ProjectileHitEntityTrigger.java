package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.projectiles;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.Location;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Map;
import java.util.UUID;

public class ProjectileHitEntityTrigger extends Trigger {
    private final EntityType projectile;

    public ProjectileHitEntityTrigger(CustomEnchant customEnchant, EnchantTriggerType type){
        super(customEnchant, type);
        this.projectile = null;
    }
    public ProjectileHitEntityTrigger(CustomEnchant customEnchant, EnchantTriggerType type, EntityType projectile) {
        super(customEnchant, type);
        this.projectile = projectile;
    }


    @EventHandler
    public void onProjectileHitEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Projectile usedProjectile))
            return;
        if (projectile != null && usedProjectile.getType() != projectile)
            return;
        if (!(usedProjectile.getShooter() instanceof Player player))
            return;

        String projectileUniqueTag = "projectile_" + UUID.randomUUID().toString().substring(0, 8);
        String entityUniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);

        usedProjectile.addScoreboardTag(projectileUniqueTag);
        e.getEntity().addScoreboardTag(entityUniqueTag);

        Location arrowLoc = usedProjectile.getLocation();
        executeCommands(e, player, e.getEntity().getType().name(), Map.of(
                "entity", e.getEntity().getType().name(),
                "entity_tag", entityUniqueTag,
                "entity_x", String.valueOf(e.getEntity().getLocation().getX()),
                "entity_y", String.valueOf(e.getEntity().getLocation().getY()),
                "entity_z", String.valueOf(e.getEntity().getLocation().getZ()),
                "projectile_x", String.valueOf(arrowLoc.getX()),
                "projectile_y", String.valueOf(arrowLoc.getY()),
                "projectile_z", String.valueOf(arrowLoc.getZ()),
                "projectile_tag", projectileUniqueTag
        ));
    }
}
