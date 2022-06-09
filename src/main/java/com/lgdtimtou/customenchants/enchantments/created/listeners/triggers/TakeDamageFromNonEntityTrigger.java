package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;

public class TakeDamageFromNonEntityTrigger extends Trigger{
    public TakeDamageFromNonEntityTrigger(Enchantment enchantment) {
        super(enchantment);
    }

    @EventHandler
    public void onTakeDamageFromNonEntity(EntityDamageEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;
        executeCommands(e, player, null, Map.of());
    }
}
