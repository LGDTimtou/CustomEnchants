package be.timonc.customenchantments.enchantments.created.triggers.movement;

import be.timonc.customenchantments.Main;
import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import be.timonc.customenchantments.other.File;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerIdleTrigger implements TriggerListener {

    private static final Map<Player, Long> lastMoveTime = new HashMap<>();
    private final TriggerInvoker triggerInvoker;

    public PlayerIdleTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
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
                            triggerInvoker.trigger(null, player, Map.of(
                                    new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "idle_time"),
                                    timePassedSeconds,
                                    new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "idle_time"),
                                    timePassedSeconds,
                                    new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "idle_time"),
                                    timePassedSeconds
                            ), Map.of());
                        }
                    });
                },
                20L, File.ConfigValue.PLAYER_IDLE_TRIGGER_CHECK_FREQUENCY.getDouble().longValue() * 20L
        );
    }
}
