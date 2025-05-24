package com.lgdtimtou.customenchantments.enchantments.created.triggers.projectiles;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;

public class ProjectileHitEntityTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public ProjectileHitEntityTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onProjectileHitEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Projectile usedProjectile))
            return;
        if (!(usedProjectile.getShooter() instanceof Player player))
            return;

        Entity entity = e.getEntity();

        String projectileUniqueTag = "projectile_" + UUID.randomUUID().toString().substring(0, 8);
        String entityUniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);

        usedProjectile.addScoreboardTag(projectileUniqueTag);
        entity.addScoreboardTag(entityUniqueTag);

        triggerInvoker.trigger(
                e,
                player,
                Map.of(
                        new ConditionKey(TriggerConditionType.ENTITY, "entity"), entity,
                        new ConditionKey(TriggerConditionType.ENTITY, "projectile"), usedProjectile
                ),
                Map.of("entity_tag", () -> entityUniqueTag, "projectile_tag", () -> projectileUniqueTag),
                () -> {
                    usedProjectile.removeScoreboardTag(projectileUniqueTag);
                    entity.removeScoreboardTag(entityUniqueTag);
                }
        );
    }
}
