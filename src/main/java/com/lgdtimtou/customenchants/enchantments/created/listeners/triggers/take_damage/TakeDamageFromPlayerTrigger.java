package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.take_damage;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class TakeDamageFromPlayerTrigger extends Trigger {
    public TakeDamageFromPlayerTrigger(Enchantment enchantment) {
        super(enchantment);
    }

    @EventHandler
    public void onTakeDamageFromPlayer(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;
        if (!(e.getDamager() instanceof Player damager))
            return;
        executeCommands(e, player, null, Map.of("damager", damager.getDisplayName()));
    }
}
