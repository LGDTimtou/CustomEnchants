package be.timonc.customenchantments.enchantments.defaultenchants;

import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;

import java.util.Set;

public class DefaultTriggerListener extends TriggerListener {


    protected DefaultTriggerListener() {
        super(null);
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of();
    }
}
