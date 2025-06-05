package be.timonc.customenchantments.enchantments.custom.triggers.click;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.Set;

public class RightClickBlockTrigger extends TriggerListener {

    private final TriggerConditionGroup clickedBlockConditions = new TriggerConditionGroup(
            "clicked", TriggerConditionGroupType.BLOCK
    );

    public RightClickBlockTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onPlayerRightClickBlock(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) return;

        triggerInvoker.trigger(event, event.getPlayer(), Map.of(clickedBlockConditions, event.getClickedBlock()));
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(clickedBlockConditions);
    }
}
