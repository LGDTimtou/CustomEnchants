package be.timonc.customenchantments.enchantments.custom.triggers.kill;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.Set;

public class KillPlayerTrigger extends TriggerListener {

    private final TriggerConditionGroup killedPlayerConditions = new TriggerConditionGroup(
            "killed", TriggerConditionGroupType.PLAYER
    );

    public KillPlayerTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player killer)) return;
        if (!(e.getEntity() instanceof Player killed)) return;

        triggerInvoker.trigger(e, killer, Map.of(killedPlayerConditions, killed));
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(killedPlayerConditions);
    }
}
