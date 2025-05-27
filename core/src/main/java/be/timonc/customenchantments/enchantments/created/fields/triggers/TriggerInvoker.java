package be.timonc.customenchantments.enchantments.created.fields.triggers;

import be.timonc.customenchantments.enchantments.created.fields.Trigger;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerCondition;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public class TriggerInvoker {


    private final Set<Trigger> subscribers = new HashSet<>();
    private final TriggerType triggerType;
    private final Set<TriggerCondition> triggerConditions;


    public TriggerInvoker(TriggerType triggerType) {
        this.triggerType = triggerType;
        triggerConditions = triggerType.createInstance();
    }

    public TriggerCondition getCondition(TriggerConditionType type, String prefix) {
        return triggerConditions.stream()
                                .filter(triggerCondition -> triggerCondition.getType() == type && triggerCondition.getPrefix()
                                                                                                                  .equals(prefix))
                                .findFirst()
                                .orElse(null);
    }

    public Set<TriggerType> getOverriddenBy() {
        return triggerType.getOverriddenBy();
    }

    public void trigger(Event event, Player player, Map<TriggerCondition, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters) {
        trigger(event, player, Collections.emptySet(), triggerConditionMap, localParameters);
    }

    public void trigger(Event event, Player player, Map<TriggerCondition, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters, Runnable onComplete) {
        trigger(event, player, Collections.emptySet(), triggerConditionMap, localParameters, onComplete);
    }

    public void trigger(Event event, Player player, Set<ItemStack> priorityItems, Map<TriggerCondition, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters) {
        trigger(event, player, priorityItems, triggerConditionMap, localParameters, () -> {});
    }

    public void trigger(Event event, Player player, Set<ItemStack> priorityItems, Map<TriggerCondition, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters, Runnable onComplete) {
        //Adding all global trigger conditions and its value to the trigger condition map
        Map<TriggerCondition, Object> mutableTriggerConditionMap = new HashMap<>(triggerConditionMap);
        TriggerCondition.getGlobalTriggerConditions()
                        .forEach(condition -> mutableTriggerConditionMap.put(
                                condition,
                                condition.getGlobalValue(player)
                        ));

        //Adding all trigger condition parameters to the parameter map
        Map<String, Supplier<String>> parameters = new HashMap<>(localParameters);
        mutableTriggerConditionMap.forEach((triggerCondition, obj) ->
                parameters.putAll(triggerCondition.getParameters(obj))
        );

        subscribers.forEach(trigger -> trigger.executeInstructions(
                event,
                player,
                priorityItems,
                parameters,
                mutableTriggerConditionMap,
                onComplete
        ));
    }

    public void subscribe(Trigger trigger) {
        subscribers.add(trigger);
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }
}
