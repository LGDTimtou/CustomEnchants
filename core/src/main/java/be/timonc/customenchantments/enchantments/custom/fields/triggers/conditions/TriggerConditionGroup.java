package be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TriggerConditionGroup {

    private final TriggerConditionGroupType type;
    private final Set<TriggerCondition> conditions;
    private final Function<Object, Map<TriggerCondition, Supplier<Object>>> conditionObjectMapSupplier;

    public TriggerConditionGroup(String prefix, TriggerConditionGroupType type) {
        this.type = type;
        this.conditions = type.getConditionSupplier(prefix);
        this.conditionObjectMapSupplier = type.getConditionMapSupplier(prefix);
    }

    public TriggerConditionGroup(GlobalTriggerCondition globalTriggerCondition) {
        this(globalTriggerCondition.getPrefix(), globalTriggerCondition.getGroupType());
    }

    public static Set<TriggerCondition> getFlatTriggerConditions(Set<TriggerConditionGroup> groups) {
        return groups.stream().flatMap(group -> group.getConditions().stream()).collect(Collectors.toSet());
    }

    public TriggerConditionGroupType getType() {
        return type;
    }

    public Set<TriggerCondition> getConditions() {
        return conditions;
    }

    public Map<TriggerCondition, Object> getConditionMap(Object object) {
        return conditionObjectMapSupplier.apply(object).entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().get()
        ));
    }

    public Map<String, Supplier<String>> getParameters(Object object) {
        return conditionObjectMapSupplier.apply(object).entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey().name(),
                entry -> () -> entry.getValue().get().toString()
        ));
    }
}
