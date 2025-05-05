package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.UUID;

public class KillMobTrigger extends Trigger {

    public KillMobTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }


    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player killer)) return;
        if (!(e.getEntity() instanceof Monster))
            return;

        Entity entity = e.getEntity();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        entity.addScoreboardTag(uniqueTag);
        executeCommands(
                e,
                killer,
                Map.of(new ConditionKey(TriggerConditionType.ENTITY, "mob"), entity),
                Map.of("mob_tag", () -> uniqueTag),
                () -> entity.removeScoreboardTag(uniqueTag)
        );
    }
}
