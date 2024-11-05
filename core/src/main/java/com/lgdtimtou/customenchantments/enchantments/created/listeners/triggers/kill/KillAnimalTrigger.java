package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillAnimalTrigger extends Trigger {
    public KillAnimalTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onAnimalKill(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Animals))
            return;
        executeCommands(e, e.getEntity().getKiller(), e.getEntity().getType().name(), Map.of("animal", e.getEntity().getType().name()));
    }
}
