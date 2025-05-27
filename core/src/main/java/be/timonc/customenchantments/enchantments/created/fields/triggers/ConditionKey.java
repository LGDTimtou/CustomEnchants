package be.timonc.customenchantments.enchantments.created.fields.triggers;

import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;

public record ConditionKey(TriggerConditionType type, String prefix) {


    @Override
    public String toString() {
        return prefix.isEmpty() ? type.name().toLowerCase() : prefix + "_" + type.name().toLowerCase();
    }
}

