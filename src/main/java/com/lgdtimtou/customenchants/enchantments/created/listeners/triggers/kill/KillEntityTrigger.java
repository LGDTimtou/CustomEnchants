package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.kill;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillEntityTrigger extends Trigger {
    public KillEntityTrigger(Enchantment enchantment){
        super(enchantment);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e){
        executeCommands(e, e.getEntity().getKiller(), e.getEntity().getType().name(), Map.of("entity", e.getEntity().getType().name()));
    }
}
