package be.timonc.customenchantments.enchantments.custom.triggers.block_other;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ChangeSignTrigger extends TriggerListener {

    private final TriggerConditionGroup linesConditions = new TriggerConditionGroup(
            "lines", TriggerConditionGroupType.STRING
    );

    public ChangeSignTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onSignChance(SignChangeEvent e) {
        String lines = Arrays.toString(e.getLines());
        triggerInvoker.trigger(
                e,
                e.getPlayer(),
                Map.of(linesConditions, lines)
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(linesConditions);
    }
}
