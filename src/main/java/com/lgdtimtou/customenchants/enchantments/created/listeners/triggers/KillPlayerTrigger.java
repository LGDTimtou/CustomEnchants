package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillPlayerTrigger extends Trigger {

    public KillPlayerTrigger(Enchantment enchantment) {
        super(enchantment);
    }

    @EventHandler
    public void onKill(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Player killed))
            return;
        executeCommands(e, killed.getKiller(), null, Map.of("killed", killed.getDisplayName()));
    }


}
