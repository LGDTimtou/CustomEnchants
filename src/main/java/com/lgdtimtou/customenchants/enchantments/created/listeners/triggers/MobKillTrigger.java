package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public class MobKillTrigger extends Trigger{

    public MobKillTrigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels) {
        super(enchantment, levels);
    }


    @EventHandler
    public void onMobKill(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Monster))
            return;
        if (!defaultChecks(e.getEntity().getKiller()))
            return;
        dispatchCommands();
    }

}
