package com.lgdtimtou.customenchantments.enchantments.created.triggers.kill;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillPlayerTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public KillPlayerTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player killer)) return;
        if (!(e.getEntity() instanceof Player killed)) return;
        triggerType.trigger(e, killer, Map.of(
                new ConditionKey(TriggerConditionType.PLAYER, "killed"), killed
        ), Map.of());
    }
}
