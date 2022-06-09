package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class TakeDamageFromMobTrigger extends Trigger{
    public TakeDamageFromMobTrigger(Enchantment enchantment) {
        super(enchantment);
    }

    @EventHandler
    public void onTakeDamageFromMob(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;
        if (!(e.getDamager() instanceof Monster))
            return;
        executeCommands(e, player, Map.of());
    }
}
