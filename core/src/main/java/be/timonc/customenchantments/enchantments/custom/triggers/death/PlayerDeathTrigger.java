package be.timonc.customenchantments.enchantments.custom.triggers.death;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PlayerDeathTrigger extends TriggerListener {

    private final TriggerConditionGroup deadPlayerConditions = new TriggerConditionGroup(
            "dead", TriggerConditionGroupType.PLAYER
    );
    private final TriggerConditionGroup lastDamageConditions = new TriggerConditionGroup(
            "last_damage", TriggerConditionGroupType.CAUSE
    );


    public PlayerDeathTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Bukkit.getOnlinePlayers().forEach(otherPlayer -> {
            if (player != otherPlayer) {
                triggerInvoker.trigger(
                        event, otherPlayer, Map.of(
                                deadPlayerConditions,
                                player,
                                lastDamageConditions,
                                Objects.requireNonNull(event.getEntity().getLastDamageCause()).getCause()
                        )
                );
            }
        });
    }


    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(deadPlayerConditions, lastDamageConditions);
    }
}
