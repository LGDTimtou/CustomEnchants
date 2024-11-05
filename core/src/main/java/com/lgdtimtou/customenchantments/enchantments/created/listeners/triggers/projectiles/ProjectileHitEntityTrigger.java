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
    public void onProjectileHitEntity(ProjectileHitEvent e){
        if (projectile != null && e.getEntity().getType() != projectile)
            return;
        if (!(e.getEntity().getShooter() instanceof Player player))
            return;
        if (e.getHitEntity() == null)
            return;

        Location arrowLoc = e.getEntity().getLocation();
        executeCommands(e, player, e.getHitEntity().getType().name(), Map.of(
                "hit_entity", e.getHitEntity().getType().name(),
                "x_" + projectile.name(), String.valueOf(arrowLoc.getX()),
                "y_" + projectile.name(), String.valueOf(arrowLoc.getY()),
                "z_" + projectile.name(), String.valueOf(arrowLoc.getZ())
        ));
    }
}
