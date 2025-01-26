package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.UUID;

public class KillAnimalTrigger extends Trigger {
    public KillAnimalTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onAnimalKill(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Animals))
            return;

        Entity entity = e.getEntity();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        entity.addScoreboardTag(uniqueTag);
        executeCommands(e, e.getEntity().getKiller(), e.getEntity().getType().name(), Map.of(
                "animal", e.getEntity().getType().name(),
                "entity_tag", uniqueTag,
                "entity_x", String.valueOf(e.getEntity().getLocation().getX()),
                "entity_y", String.valueOf(e.getEntity().getLocation().getY()),
                "entity_z", String.valueOf(e.getEntity().getLocation().getZ())
        ), () -> entity.removeScoreboardTag(uniqueTag));
    }
}
