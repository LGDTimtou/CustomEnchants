package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillEntityTrigger extends Trigger{
    public KillEntityTrigger(Enchantment enchantment) {
        super(enchantment);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e){
        executeCommands(e, e.getEntity().getKiller(), Map.of());
    }
}
