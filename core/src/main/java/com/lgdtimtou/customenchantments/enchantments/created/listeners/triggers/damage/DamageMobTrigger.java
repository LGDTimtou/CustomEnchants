package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.damage;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class DamageMobTrigger extends Trigger {
    public DamageMobTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
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
