package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public class PlayerKillTrigger extends Trigger {
    public PlayerKillTrigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels) {
        super(enchantment, levels);
    }

    @EventHandler
    public void onKill(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Player player))
            return;
        if (!defaultChecks(player, e.getEntity().getKiller()))
            return;

        getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }


}
