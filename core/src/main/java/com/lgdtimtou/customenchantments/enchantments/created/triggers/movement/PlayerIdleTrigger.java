package com.lgdtimtou.customenchantments.enchantments.created.triggers.movement;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerIdleTrigger implements CustomEnchantListener {

    private static final Map<Player, Long> lastMoveTime = new HashMap<>();
    private static final int idleTimeSeconds = 10; // Idle time in seconds
    private final TriggerType triggerType;

    public PlayerIdleTrigger(TriggerType type) {
        this.triggerType = type;
        startIdleCheckTask();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        lastMoveTime.put(player, System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        lastMoveTime.remove(event.getPlayer());
    }

    private void startIdleCheckTask() {
        Bukkit.getScheduler().runTaskTimer(
                Main.getMain(),
                () -> {
                    long currentTime = System.currentTimeMillis();
                    lastMoveTime.forEach((player, lastTime) -> {
                        if ((currentTime - lastTime) >= (idleTimeSeconds * 1000L)) {
                            triggerIdle(player);
                        }
                    });
                },
                20L, 10L
        );
    }

    private void triggerIdle(Player player) {
        triggerType.trigger(null, player, Map.of(), Map.of()); // Trigger idle event
    }
}
