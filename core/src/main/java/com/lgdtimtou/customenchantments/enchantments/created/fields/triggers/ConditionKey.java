package com.lgdtimtou.customenchantments.enchantments.created.fields.triggers;

public record ConditionKey(TriggerConditionType type, String prefix) {


    @Override
    public String toString() {
        return prefix.isEmpty() ? type.name().toLowerCase() : prefix + "_" + type.name().toLowerCase();
    }
}

