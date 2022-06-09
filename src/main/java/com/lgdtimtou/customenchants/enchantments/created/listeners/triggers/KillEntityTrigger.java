package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.Map;

public class KillEntityTrigger extends Trigger{
    public KillEntityTrigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels) {
        super(enchantment);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e){
        if (!defaultChecks(e, e.getEntity().getKiller()))
            return;

        dispatchCommands(e.getEntity().getKiller(), Map.of());
    }
}
