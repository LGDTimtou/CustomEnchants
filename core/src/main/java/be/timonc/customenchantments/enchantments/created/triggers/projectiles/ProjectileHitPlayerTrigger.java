package be.timonc.customenchantments.enchantments.created.triggers.projectiles;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ProjectileHitPlayerTrigger extends TriggerListener {

    private final TriggerConditionGroup hitPlayerConditions = new TriggerConditionGroup(
            "hit", TriggerConditionGroupType.PLAYER
    );
    private final TriggerConditionGroup projectileConditions = new TriggerConditionGroup(
            "projectile", TriggerConditionGroupType.ENTITY
    );

    public ProjectileHitPlayerTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
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
                        hitPlayerConditions, victim,
                        projectileConditions, usedProjectile
                ),
                Map.of("projectile_tag", () -> projectileUniqueTag),
                () -> usedProjectile.removeScoreboardTag(projectileUniqueTag)
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(hitPlayerConditions, projectileConditions);
    }
}
