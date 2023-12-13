package com.lgdtimtou.customenchants.customevents.health_change;

import com.lgdtimtou.customenchants.customevents.health_decrease.PlayerHealthDecreaseEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class PlayerHealthChangeListeners implements Listener {


    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;
        Event healthChangeEvent = new PlayerHealthChangeEvent(player, -e.getDamage());
        Event healthDecreaseEvent = new PlayerHealthDecreaseEvent(player, e.getDamage());
        Bukkit.getPluginManager().callEvent(healthChangeEvent);
        Bukkit.getPluginManager().callEvent(healthDecreaseEvent);
    }

    @EventHandler
    public void onPlayerTakeDamageByEntity(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;
        Event healthChangeEvent = new PlayerHealthChangeEvent(player, -e.getDamage());
        Event healthDecreaseEvent = new PlayerHealthDecreaseEvent(player, e.getDamage());
        Bukkit.getPluginManager().callEvent(healthChangeEvent);
        Bukkit.getPluginManager().callEvent(healthDecreaseEvent);
    }

    @EventHandler
    public void onPlayerRegenHealth(EntityRegainHealthEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;
        Event event = new PlayerHealthChangeEvent(player, e.getAmount());
        Bukkit.getPluginManager().callEvent(event);
    }


}
