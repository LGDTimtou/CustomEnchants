package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.damage;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class DamagePlayerTrigger extends Trigger {

    public DamagePlayerTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player damaged))
            return;
        if (!(e.getDamager() instanceof Player player))
            return;
        executeCommands(e, player, damaged.getDisplayName(), Map.of("damaged", damaged.getDisplayName()));
    }
}
