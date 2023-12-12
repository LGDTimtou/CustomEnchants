package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.take_damage;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class TakeDamageFromEntityTrigger extends Trigger {
    public TakeDamageFromEntityTrigger(Enchantment enchantment) {
        super(enchantment);
    }

    @EventHandler
    public void onDamageFromEntity(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;

        executeCommands(e, player, null, Map.of());
    }
}
