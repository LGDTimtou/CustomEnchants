package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.kill;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillMobTrigger extends Trigger {

    public KillMobTrigger(Enchantment enchantment){
        super(enchantment);
    }


    @EventHandler
    public void onMobKill(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Monster))
            return;
        executeCommands(e, e.getEntity().getKiller(), e.getEntity().getType().name(), null, Map.of());
    }

}
