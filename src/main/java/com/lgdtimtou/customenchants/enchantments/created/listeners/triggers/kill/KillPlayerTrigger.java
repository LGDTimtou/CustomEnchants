package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.kill;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillPlayerTrigger extends Trigger {

    public KillPlayerTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
    }

    @EventHandler
    public void onKill(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Player killed))
            return;
        executeCommands(e, killed.getKiller(), killed.getDisplayName(), Map.of("killed", killed.getDisplayName()));
    }

}
