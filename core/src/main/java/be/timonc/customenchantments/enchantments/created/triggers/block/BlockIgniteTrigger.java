package be.timonc.customenchantments.enchantments.created.triggers.block;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockIgniteEvent;

import java.util.Map;
import java.util.Set;

public class BlockIgniteTrigger extends TriggerListener {

    private final TriggerConditionGroup ignitedBlockConditions = new TriggerConditionGroup(
            "ignited", TriggerConditionGroupType.BLOCK
    );
    private final TriggerConditionGroup igniteCauseConditions = new TriggerConditionGroup(
            "ignite", TriggerConditionGroupType.CAUSE
    );

    public BlockIgniteTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        if (e.getPlayer() == null) return;

        triggerInvoker.trigger(
                e,
                e.getPlayer(),
                Map.of(
                        ignitedBlockConditions, e.getBlock(),
                        igniteCauseConditions, e.getCause()
                )
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(ignitedBlockConditions, igniteCauseConditions);
    }
}

