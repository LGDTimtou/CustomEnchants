package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.take_damage;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class TakeDamageFromMobTrigger extends Trigger {
    public TakeDamageFromMobTrigger(Enchantment enchantment){
        super(enchantment);
    }

    @EventHandler
    public void onTakeDamageFromMob(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;
        if (!(e.getDamager() instanceof Monster monster))
            return;
        executeCommands(e, player, monster.getType().name(), Map.of("mob", monster.getType().name()));
    }
}
