package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

public class TriggerCondition<T extends Enum<T>> {

    Class<T> condition_enum;
    TriggerCondition(Class<T> condition_enum){
        this.condition_enum = condition_enum;
    }

    Class<T> enumClass() { return condition_enum; }

}
