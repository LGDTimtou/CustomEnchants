package com.lgdtimtou.customenchantments.enchantments.created.fields.triggers;

import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public class TriggerInvoker {


    private final Set<CustomEnchantTrigger> subscribers = new HashSet<>();
    private final TriggerType triggerType;
    private CustomEnchantListener instance;


    public TriggerInvoker(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public Set<TriggerType> getOverriddenBy() {
        return triggerType.getOverriddenBy();
    }

    public void trigger(Event event, Player player, Map<ConditionKey, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters) {
        trigger(event, player, Collections.emptySet(), triggerConditionMap, localParameters);
    }

    public void trigger(Event event, Player player, Map<ConditionKey, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters, Runnable onComplete) {
        trigger(event, player, Collections.emptySet(), triggerConditionMap, localParameters, onComplete);
    }

    public void trigger(Event event, Player player, Set<ItemStack> priorityItems, Map<ConditionKey, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters) {
        trigger(event, player, priorityItems, triggerConditionMap, localParameters, () -> {});
    }

    public void trigger(Event event, Player player, Set<ItemStack> priorityItems, Map<ConditionKey, Object> triggerConditionMap, Map<String, Supplier<String>> localParameters, Runnable onComplete) {
        subscribers.forEach(customEnchantTrigger -> customEnchantTrigger.executeInstructions(
                event,
                player,
                priorityItems,
                new HashMap<>(triggerConditionMap),
                new HashMap<>(localParameters),
                onComplete
        ));
    }

    public void subscribe(CustomEnchantTrigger customEnchantTrigger) {
        createInstance();
        subscribers.add(customEnchantTrigger);
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    private void createInstance() {
        try {
            if (this.instance == null) {
                this.instance = (CustomEnchantListener) triggerType.getConstructor().newInstance(this);
                Util.registerListener(this.instance);
            }
        } catch (Exception ignored) {
            Util.error("Trigger type: " + this + " could not be instanced! Please report this error!");
            this.instance = null;
        }
    }
}
