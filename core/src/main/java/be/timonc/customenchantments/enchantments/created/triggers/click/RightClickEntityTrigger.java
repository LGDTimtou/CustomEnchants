package be.timonc.customenchantments.enchantments.created.triggers.click;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Map;
import java.util.Set;

public class RightClickEntityTrigger extends TriggerListener {

    private final TriggerConditionGroup clickedEntityConditions = new TriggerConditionGroup(
            "clicked", TriggerConditionGroupType.ENTITY
    );

    public RightClickEntityTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onPlayerRightClickEntity(PlayerInteractEntityEvent event) {
        triggerInvoker.trigger(event, event.getPlayer(), Map.of(clickedEntityConditions, event.getRightClicked()));
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(clickedEntityConditions);
    }
}
