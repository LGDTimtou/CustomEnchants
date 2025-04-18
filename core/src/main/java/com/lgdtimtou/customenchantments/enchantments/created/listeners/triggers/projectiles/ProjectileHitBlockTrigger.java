package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.projectiles;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Map;
import java.util.UUID;

public class ProjectileHitBlockTrigger extends Trigger {
    public ProjectileHitBlockTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
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

        executeCommands(
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
