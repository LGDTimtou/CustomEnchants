package be.timonc.customenchantments.enchantments.created.triggers.block;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;
import java.util.Set;

public class BlockBreakTrigger extends TriggerListener {

    private final TriggerConditionGroup brokenBlockConditions = new TriggerConditionGroup(
            "broken", TriggerConditionGroupType.BLOCK
    );

    public BlockBreakTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        triggerInvoker.trigger(
                e,
                e.getPlayer(),
                Map.of(brokenBlockConditions, e.getBlock())
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(brokenBlockConditions);
    }
}
