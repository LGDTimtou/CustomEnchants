package com.lgdtimtou.customenchantments.enchantments.created.triggers.take_damage;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;

public class TakeDamageFromMobTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public TakeDamageFromMobTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onTakeDamageFromMob(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        if (!(e.getDamager() instanceof Monster monster))
            return;

        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        monster.addScoreboardTag(uniqueTag);

        triggerType.trigger(e, player,
                Map.of(
                        new ConditionKey(TriggerConditionType.ENTITY, "mob"), monster,
                        new ConditionKey(TriggerConditionType.CAUSE, "damage"), e.getCause()
                ),
                Map.of("mob_tag", () -> uniqueTag),
                () -> monster.removeScoreboardTag(uniqueTag)
        );
    }
}
