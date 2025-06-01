package be.timonc.customenchantments.enchantments.created.fields.triggers;

import be.timonc.customenchantments.enchantments.created.fields.Trigger;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.GlobalTriggerCondition;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerCondition;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public class TriggerInvoker {


    private final Set<Trigger> subscribers = new HashSet<>();

    public void trigger(Event event, Player player) {
        trigger(event, player, Map.of(), Map.of());
    }

    public void trigger(Event event, Player player, Map<TriggerConditionGroup, Object> triggerConditionMap) {
        trigger(event, player, triggerConditionMap, Map.of());
    }

    public void trigger(Event event, Player player, Map<TriggerConditionGroup, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters) {
        trigger(event, player, Collections.emptySet(), triggerConditionMap, localParameters);
    }

    public void trigger(Event event, Player player, Map<TriggerConditionGroup, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters, Runnable onComplete) {
        trigger(event, player, Collections.emptySet(), triggerConditionMap, localParameters, onComplete);
    }

    public void trigger(Event event, Player player, Set<ItemStack> priorityItems, Map<TriggerConditionGroup, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters) {
        trigger(event, player, priorityItems, triggerConditionMap, localParameters, () -> {});
    }

    public void trigger(Event event, Player player, Set<ItemStack> priorityItems, Map<TriggerConditionGroup, Object> triggerConditionGroupMap, Map<String, Supplier<String>> localParameters, Runnable onComplete) {
        //Adding all global trigger conditions and its value to the group trigger condition map
        Map<TriggerConditionGroup, Object> mutableTriggerConditionGroupMap = new HashMap<>(triggerConditionGroupMap);
        mutableTriggerConditionGroupMap.putAll(GlobalTriggerCondition.getMap(player));

        Map<TriggerCondition, Object> triggerConditionObjectMap = new HashMap<>();
        Map<String, Supplier<String>> parameters = new HashMap<>(localParameters);

        mutableTriggerConditionGroupMap.forEach((triggerConditionGroup, object) -> {
                    //Adding all group trigger conditions to the trigger condition map
                    Map<TriggerCondition, Object> conditionsMap = triggerConditionGroup.getConditionMap(object);
                    triggerConditionObjectMap.putAll(conditionsMap);

                    //Adding all trigger condition parameters to the parameter map
                    parameters.putAll(triggerConditionGroup.getParameters(object));
                }
        );

        subscribers.forEach(trigger -> trigger.executeInstructions(
                event,
                player,
                priorityItems,
                parameters,
                triggerConditionObjectMap,
                onComplete
        ));
    }

    public void subscribe(Trigger trigger) {
        subscribers.add(trigger);
    }
}
