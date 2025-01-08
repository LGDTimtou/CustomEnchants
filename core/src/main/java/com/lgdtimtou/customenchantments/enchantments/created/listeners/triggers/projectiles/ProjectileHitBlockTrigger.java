package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.projectiles;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.Location;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Map;
import java.util.UUID;

public class ProjectileHitBlockTrigger extends Trigger {

    private final EntityType projectile;

    public ProjectileHitBlockTrigger(CustomEnchant customEnchant, EnchantTriggerType type){
        super(customEnchant, type);
        this.projectile = null;
    }
    public ProjectileHitBlockTrigger(CustomEnchant customEnchant, EnchantTriggerType type, EntityType projectile) {
        super(customEnchant, type);
        this.projectile = projectile;
    }

    @EventHandler
    public void onProjectileHitBlock(ProjectileHitEvent e){
        if (projectile != null && e.getEntity().getType() != projectile)
            return;
        if (!(e.getEntity().getShooter() instanceof Player player))
            return;
        if (e.getHitBlock() == null)
            return;

        String projectileUniqueTag = "projectile_" + UUID.randomUUID().toString().substring(0, 8);
        e.getEntity().addScoreboardTag(projectileUniqueTag);

        Location arrowLoc = e.getEntity().getLocation();
        executeCommands(e, player, e.getHitBlock().getType().name(), Map.of(
                "block", e.getHitBlock().getType().name(),
                "projectile_x", String.valueOf(arrowLoc.getX()),
                "projectile_y", String.valueOf(arrowLoc.getY()),
                "projectile_z", String.valueOf(arrowLoc.getZ()),
                "projectile_tag", projectileUniqueTag
        ));
    }
}
