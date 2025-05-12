package com.lgdtimtou.customenchantments.enchantments.created.triggers.projectiles;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Map;
import java.util.UUID;

public class ProjectileHitBlockTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public ProjectileHitBlockTrigger(TriggerType type) {
        this.triggerType = type;
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

        triggerType.trigger(
                e,
                player,
                Map.of(
                        new ConditionKey(TriggerConditionType.BLOCK, ""),
                        e.getHitBlock(),
                        new ConditionKey(TriggerConditionType.ENTITY, "projectile"),
                        entity
                ),
                Map.of("projectile_tag", () -> projectileUniqueTag),
                () -> entity.removeScoreboardTag(projectileUniqueTag)
        );
    }
}
