package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillEntityTrigger extends Trigger {
    public KillEntityTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e){
        executeCommands(e, e.getEntity().getKiller(), e.getEntity().getType().name(), Map.of("entity", e.getEntity().getType().name()));
    }
}
