package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillMobTrigger extends Trigger {

    public KillMobTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }


    @EventHandler
    public void onMobKill(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Monster))
            return;
        executeCommands(e, e.getEntity().getKiller(), e.getEntity().getType().name(), Map.of("mob", e.getEntity().getType().name()));
    }

}
