package be.timonc.customenchantments.enchantments.created.triggers.movement;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;
import java.util.Set;

public class PlayerSwimTrigger extends TriggerListener {

    private final TriggerConditionGroup fromXConditions = new TriggerConditionGroup(
            "from_x", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup fromYConditions = new TriggerConditionGroup(
            "from_y", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup fromZConditions = new TriggerConditionGroup(
            "from_z", TriggerConditionGroupType.NUMBER
    );

    public PlayerSwimTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onSwimEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (!player.isSwimming()) return;
        triggerInvoker.trigger(e, e.getPlayer(), Map.of(
                fromXConditions, e.getFrom().getX(),
                fromYConditions, e.getFrom().getY(),
                fromZConditions, e.getFrom().getZ()
        ));
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(fromXConditions, fromYConditions, fromZConditions);
    }
}
