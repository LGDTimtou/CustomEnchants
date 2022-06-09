package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.Map;

public class KillPlayerTrigger extends Trigger {

    public KillPlayerTrigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels) {
        super(enchantment);
    }

    @EventHandler
    public void onKill(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Player killed))
            return;
        if (!defaultChecks(e, killed.getKiller()))
            return;


        dispatchCommands(killed.getKiller(), Map.of("killed", killed.getDisplayName()));
    }


}
