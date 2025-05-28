package be.timonc.customenchantments.enchantments.created.triggers.movement;

import be.timonc.customenchantments.Main;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import be.timonc.customenchantments.other.File;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerIdleTrigger extends TriggerListener {

    private static final Map<Player, Long> lastMoveTime = new HashMap<>();
    private final TriggerConditionGroup idleTimeConditions = new TriggerConditionGroup(
            "idle_time", TriggerConditionGroupType.NUMBER
    );

    public PlayerIdleTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
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
                            triggerInvoker.trigger(
                                    null,
                                    player,
                                    Map.of(idleTimeConditions, timePassedSeconds)
                            );
                        }
                    });
                },
                20L, File.ConfigValue.PLAYER_IDLE_TRIGGER_CHECK_FREQUENCY.getDouble().longValue() * 20L
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(idleTimeConditions);
    }
}
