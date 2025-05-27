package be.timonc.customenchantments.enchantments.created.fields.triggers.conditions;

import be.timonc.customenchantments.other.Util;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TriggerCondition {

    private static final Set<TriggerCondition> globalTriggerConditions = Arrays.stream(GlobalTriggerCondition.values())
                                                                               .map(
                                                                                       TriggerCondition::new)
                                                                               .collect(
                                                                                       Collectors.toSet());
    private final TriggerConditionType type;
    private final String prefix;
    private Function<Player, Object> getGlobalValue = null;


    public TriggerCondition(TriggerConditionType type, String prefix) {
        this.type = type;
        this.prefix = prefix;
    }

    private TriggerCondition(GlobalTriggerCondition globalTriggerCondition) {
        this.type = globalTriggerCondition.getType();
        this.prefix = globalTriggerCondition.name().toLowerCase();
        this.getGlobalValue = globalTriggerCondition::getValue;
    }

    public static Set<TriggerCondition> getGlobalTriggerConditions() {
        return globalTriggerConditions;
    }

    public TriggerConditionType getType() {
        return type;
    }

    public String getPrefix() {
        return prefix;
    }

    public Map<String, Supplier<String>> getParameters(Object object) {
        return type.getConditionParameters(prefix, object);
    }

    public Object getGlobalValue(Player player) {
        if (getGlobalValue == null) {
            Util.error("Global value was null for trigger condition with type:" + type + " and prefix:" + prefix);
            return null;
        }
        return getGlobalValue.apply(player);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TriggerCondition that = (TriggerCondition) o;

        return prefix.equals(that.prefix) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + prefix.hashCode();
        return result;
    }
}
