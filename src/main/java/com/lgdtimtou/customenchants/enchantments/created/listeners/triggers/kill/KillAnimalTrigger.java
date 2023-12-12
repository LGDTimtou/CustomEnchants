package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.kill;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillAnimalTrigger extends Trigger {
    public KillAnimalTrigger(Enchantment enchantment){
        super(enchantment);
    }

    @EventHandler
    public void onAnimalKill(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Animals))
            return;
        executeCommands(e, e.getEntity().getKiller(), e.getEntity().getType().name(), null, Map.of());
    }
}
