package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.Map;

public class KillMobTrigger extends Trigger{

    public KillMobTrigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels) {
        super(enchantment);
    }


    @EventHandler
    public void onMobKill(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Monster))
            return;
        if (!defaultChecks(e, e.getEntity().getKiller()))
            return;

        dispatchCommands(e.getEntity().getKiller(), Map.of());
    }

}
