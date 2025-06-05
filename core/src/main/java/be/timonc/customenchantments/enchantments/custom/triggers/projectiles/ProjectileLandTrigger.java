package be.timonc.customenchantments.enchantments.custom.triggers.projectiles;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ProjectileLandTrigger extends TriggerListener {

    private final TriggerConditionGroup projectileConditions = new TriggerConditionGroup(
            "projectile", TriggerConditionGroupType.ENTITY
    );

    public ProjectileLandTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
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

        triggerInvoker.trigger(
                e, player, Map.of(
                        projectileConditions, usedProjectile
                ), Map.of(
                        "projectile_tag", () -> projectileUniqueTag
                ), () -> usedProjectile.removeScoreboardTag(projectileUniqueTag)
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(projectileConditions);
    }
}