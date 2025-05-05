package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillPlayerTrigger extends Trigger {

    public KillPlayerTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player killer)) return;
        if (!(e.getEntity() instanceof Player killed)) return;
        executeCommands(e, killer, Map.of(
                new ConditionKey(TriggerConditionType.PLAYER, "killed"), killed
        ), Map.of());
    }
}
