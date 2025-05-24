package com.lgdtimtou.customenchantments.enchantments.created.triggers.movement;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.Map;

public class PlayerSneakTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public PlayerSneakTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }


    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) return;
        triggerInvoker.trigger(e, e.getPlayer(), Map.of(), Map.of());
    }
}
