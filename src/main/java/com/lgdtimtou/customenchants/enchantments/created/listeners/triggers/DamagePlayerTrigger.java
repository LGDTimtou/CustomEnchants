package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;
import java.util.Map;

public class DamagePlayerTrigger extends Trigger {

    public DamagePlayerTrigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels) {
        super(enchantment, levels);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player damaged))
            return;
        if (!(e.getDamager() instanceof Player player))
            return;
        if (!defaultChecks(player))
            return;

        dispatchCommands(player, Map.of("damaged", damaged.getDisplayName()));
    }
}
