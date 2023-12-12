package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.take_damage;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;

public class TakeDamageFromNonEntityTrigger extends Trigger {
    public TakeDamageFromNonEntityTrigger(Enchantment enchantment){
        super(enchantment);
    }

    @EventHandler
    public void onTakeDamageFromNonEntity(EntityDamageEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;
        executeCommands(e, player, e.getCause().name(), null, Map.of());
    }
}
