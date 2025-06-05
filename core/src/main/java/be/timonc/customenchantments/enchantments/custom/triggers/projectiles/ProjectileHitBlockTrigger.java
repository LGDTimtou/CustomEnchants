package be.timonc.customenchantments.enchantments.custom.triggers.projectiles;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ProjectileHitBlockTrigger extends TriggerListener {

    private final TriggerConditionGroup hitBlockConditions = new TriggerConditionGroup(
            "hit", TriggerConditionGroupType.BLOCK
    );
    private final TriggerConditionGroup projectileConditions = new TriggerConditionGroup(
            "projectile", TriggerConditionGroupType.ENTITY
    );

    public ProjectileHitBlockTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onProjectileHitBlock(ProjectileHitEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player player))
            return;
        if (e.getHitBlock() == null)
            return;

        Entity entity = e.getEntity();
        String projectileUniqueTag = "projectile_" + UUID.randomUUID().toString().substring(0, 8);
        entity.addScoreboardTag(projectileUniqueTag);

        triggerInvoker.trigger(
                e,
                player,
                Map.of(
                        hitBlockConditions, e.getHitBlock(),
                        projectileConditions, entity
                ),
                Map.of("projectile_tag", () -> projectileUniqueTag),
                () -> entity.removeScoreboardTag(projectileUniqueTag)
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(hitBlockConditions, projectileConditions);
    }
}
