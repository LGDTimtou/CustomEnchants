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
    public void onProjectileLand(ProjectileHitEvent e){
        if (projectile != null && e.getEntity().getType() != projectile)
            return;
        if (!(e.getEntity().getShooter() instanceof Player player))
            return;

        Location projectileLoc = e.getEntity().getLocation();
        executeCommands(e, player, null, Map.of(
                "x_" + projectile.name(), String.valueOf(projectileLoc.getX()),
                "y_" + projectile.name(), String.valueOf(projectileLoc.getY()),
                "z_" + projectile.name(), String.valueOf(projectileLoc.getZ())
        ));
    }
}