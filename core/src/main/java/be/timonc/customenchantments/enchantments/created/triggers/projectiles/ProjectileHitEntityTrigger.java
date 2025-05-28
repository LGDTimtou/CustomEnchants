package be.timonc.customenchantments.enchantments.created.triggers.projectiles;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ProjectileHitEntityTrigger extends TriggerListener {

    private final TriggerConditionGroup hitEntityConditions = new TriggerConditionGroup(
            "hit", TriggerConditionGroupType.ENTITY
    );
    private final TriggerConditionGroup projectileConditions = new TriggerConditionGroup(
            "projectile", TriggerConditionGroupType.ENTITY
    );

    public ProjectileHitEntityTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
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
                        hitEntityConditions, entity,
                        projectileConditions, usedProjectile
                ),
                Map.of("entity_tag", () -> entityUniqueTag, "projectile_tag", () -> projectileUniqueTag),
                () -> {
                    usedProjectile.removeScoreboardTag(projectileUniqueTag);
                    entity.removeScoreboardTag(entityUniqueTag);
                }
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(hitEntityConditions, projectileConditions);
    }
}
