package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class DamageAnimalTrigger extends Trigger{
    public DamageAnimalTrigger(Enchantment enchantment) {
        super(enchantment);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Animals))
            return;
        if (!(e.getDamager() instanceof Player player))
            return;
        executeCommands(e, player, null, Map.of());
    }
}
