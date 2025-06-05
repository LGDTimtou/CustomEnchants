package be.timonc.customenchantments.enchantments.defaultenchants;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;

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
