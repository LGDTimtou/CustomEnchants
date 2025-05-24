package com.lgdtimtou.customenchantments.enchantments.created.triggers.projectiles;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;

public class ProjectileHitPlayerTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public ProjectileHitPlayerTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onProjectileHitEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Projectile usedProjectile))
            return;
        if (!(usedProjectile.getShooter() instanceof Player player))
            return;
        if (!(e.getEntity() instanceof Player victim))
            return;

        String projectileUniqueTag = "projectile_" + UUID.randomUUID().toString().substring(0, 8);
        usedProjectile.addScoreboardTag(projectileUniqueTag);

        triggerInvoker.trigger(
                e,
                player,
                Map.of(
                        new ConditionKey(TriggerConditionType.PLAYER, "victim"), victim,
                        new ConditionKey(TriggerConditionType.ENTITY, "projectile"), usedProjectile
                ),
                Map.of("projectile_tag", () -> projectileUniqueTag),
                () -> usedProjectile.removeScoreboardTag(projectileUniqueTag)
        );
    }
}
