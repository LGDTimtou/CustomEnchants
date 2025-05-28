package be.timonc.customenchantments.enchantments.created.triggers.click;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.Set;

public class LeftClickBlockTrigger extends TriggerListener {

    private final TriggerConditionGroup clickedBlockConditions = new TriggerConditionGroup(
            "clicked", TriggerConditionGroupType.BLOCK
    );

    public LeftClickBlockTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onPlayerLeftClickBlock(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.getClickedBlock() == null) return;

        triggerInvoker.trigger(event, event.getPlayer(), Map.of(clickedBlockConditions, event.getClickedBlock()));
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(clickedBlockConditions);
    }
}
