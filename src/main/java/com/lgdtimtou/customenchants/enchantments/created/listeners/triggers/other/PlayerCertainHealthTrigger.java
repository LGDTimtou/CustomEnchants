package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.other;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerCertainHealthTrigger extends Trigger {
    public PlayerCertainHealthTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
    }

    @EventHandler
    public void onDamageFromEntity(EntityDamageByEntityEvent e){
        
    }
}
