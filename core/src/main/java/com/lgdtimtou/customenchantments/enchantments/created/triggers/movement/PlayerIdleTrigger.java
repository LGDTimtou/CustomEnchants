package com.lgdtimtou.customenchantments.enchantments.created.triggers.movement;

import com.lgdtimtou.customenchantments.Main;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import com.lgdtimtou.customenchantments.other.File;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerIdleTrigger implements CustomEnchantListener {

    private static final Map<Player, Long> lastMoveTime = new HashMap<>();
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
                        double timePassedSeconds = (double) (currentTime - lastTime) / 1000L;
                        if (timePassedSeconds > 1) {
                            triggerType.trigger(null, player, Map.of(
                                    new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "time_passed"),
                                    timePassedSeconds,
                                    new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "time_passed"),
                                    timePassedSeconds,
                                    new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "time_passed"),
                                    timePassedSeconds
                            ), Map.of());
                        }
                    });
                },
                20L, File.ConfigValue.PLAYER_IDLE_TRIGGER_CHECK_FREQUENCY.getDouble().longValue() * 20L
        );
    }
}
