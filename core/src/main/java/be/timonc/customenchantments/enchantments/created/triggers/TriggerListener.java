package be.timonc.customenchantments.enchantments.created.triggers;

import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerCondition;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class TriggerListener implements Listener {

    protected Set<TriggerCondition> conditions;

    protected TriggerListener(TriggerCondition... conditions) {
        this.conditions = Arrays.stream(conditions).collect(Collectors.toSet());
        this.conditions.addAll(TriggerCondition.getGlobalTriggerConditions());
    }

    public Set<TriggerCondition> getConditions() {
        return conditions;
    }
}
