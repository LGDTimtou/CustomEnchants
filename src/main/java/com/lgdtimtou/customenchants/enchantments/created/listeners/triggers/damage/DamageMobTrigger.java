package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.damage;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class DamageMobTrigger extends Trigger {
    public DamageMobTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Monster))
            return;
        if (!(e.getDamager() instanceof Player player))
            return;
        executeCommands(e, player, e.getEntity().getType().name(), Map.of("mob", e.getEntity().getType().name()));
    }

}
