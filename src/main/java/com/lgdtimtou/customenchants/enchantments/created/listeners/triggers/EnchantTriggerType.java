package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block.*;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block_other.ActivateSculkSensorTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block_other.BellRungTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block_other.ChangeSignTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block_other.PrimeTNTTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.damage.DamageAnimalTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.damage.DamageEntityTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.damage.DamageMobTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.damage.DamagePlayerTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.fishing_rod.FishingRodCaughtTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.fishing_rod.FishingRodHitPlayerTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.health.PlayerHealthChangeTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.health.PlayerHealthDecreaseTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.health.PlayerHealthIncreaseTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.kill.KillAnimalTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.kill.KillEntityTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.kill.KillMobTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.kill.KillPlayerTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.take_damage.TakeDamageFromEntityTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.take_damage.TakeDamageFromMobTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.take_damage.TakeDamageFromNonEntityTrigger;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.take_damage.TakeDamageFromPlayerTrigger;
import com.lgdtimtou.customenchants.other.Util;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public enum EnchantTriggerType {

    PRIME_TNT(PrimeTNTTrigger.class),
    CHANGE_SIGN(ChangeSignTrigger.class),
    BELL_RUNG(BellRungTrigger.class),
    SCULK_SENSOR_ACTIVATED(ActivateSculkSensorTrigger.class),

    IGNITE_BLOCK(BlockIgniteTrigger.class),
    FERTILIZE_BLOCK(BlockFertilizeTrigger.class),
    BREAK_BLOCK(BreakBlockTrigger.class),
    PLACE_BLOCK(BlockPlaceTrigger.class),
    DAMAGE_BLOCK(BlockDamagedTrigger.class, BREAK_BLOCK),

    KILL_ENTITY(KillEntityTrigger.class),
    KILL_MOB(KillMobTrigger.class, KILL_ENTITY),
    KILL_ANIMAL(KillAnimalTrigger.class, KILL_ENTITY),
    KILL_PLAYER(KillPlayerTrigger.class, KILL_ENTITY),

    DAMAGE_ENTITY(DamageEntityTrigger.class),
    DAMAGE_MOB(DamageMobTrigger.class, DAMAGE_ENTITY),
    DAMAGE_ANIMAL(DamageAnimalTrigger.class, DAMAGE_ENTITY),
    DAMAGE_PLAYER(DamagePlayerTrigger.class, DAMAGE_ENTITY),

    TAKE_DAMAGE_FROM_NONENTITY(TakeDamageFromNonEntityTrigger.class),
    TAKE_DAMAGE_FROM_ENTITY(TakeDamageFromEntityTrigger.class),
    TAKE_DAMAGE_FROM_PLAYER(TakeDamageFromPlayerTrigger.class, TAKE_DAMAGE_FROM_ENTITY),
    TAKE_DAMAGE_FROM_MOB(TakeDamageFromMobTrigger.class, TAKE_DAMAGE_FROM_ENTITY),

    FISHING_ROD_CAUGHT(FishingRodCaughtTrigger.class),
    FISHING_ROD_HIT_PLAYER(FishingRodHitPlayerTrigger.class),

    HEALTH_CHANGE(PlayerHealthChangeTrigger.class),
    HEALTH_CHANGE_GREATER_THAN(PlayerHealthChangeTrigger.class, ConditionFunction.GREATER_THAN, ConditionType.DOUBLE, HEALTH_CHANGE),
    HEALTH_CHANGE_LESSER_THAN(PlayerHealthChangeTrigger.class, ConditionFunction.LESSER_THAN, ConditionType.DOUBLE, HEALTH_CHANGE),
    HEALTH_INCREASE(PlayerHealthIncreaseTrigger.class, HEALTH_CHANGE),
    HEALTH_INCREASE_GREATER_THAN(PlayerHealthIncreaseTrigger.class, ConditionFunction.GREATER_THAN, ConditionType.DOUBLE, HEALTH_INCREASE),
    HEALTH_INCREASE_LESSER_THAN(PlayerHealthIncreaseTrigger.class, ConditionFunction.LESSER_THAN, ConditionType.DOUBLE, HEALTH_INCREASE),
    HEALTH_DECREASE(PlayerHealthDecreaseTrigger.class, HEALTH_CHANGE),
    HEALTH_DECREASE_GREATER_THAN(PlayerHealthDecreaseTrigger.class, ConditionFunction.GREATER_THAN, ConditionType.DOUBLE, HEALTH_DECREASE),
    HEALTH_DECREASE_LESSER_THAN(PlayerHealthDecreaseTrigger.class, ConditionFunction.LESSER_THAN, ConditionType.DOUBLE, HEALTH_DECREASE);

    private final Set<EnchantTriggerType> overriddenBy;
    private Constructor<?> constructor;
    private final ConditionFunction conditionFunction;
    private final ConditionType conditionType;

    EnchantTriggerType (Class<?> triggerClass, EnchantTriggerType... overriddenBy){
        this(triggerClass, ConditionFunction.EQUALS, ConditionType.STRING, overriddenBy);
    }

    EnchantTriggerType(Class<?> triggerClass, ConditionFunction conditionFunction, ConditionType conditionType, EnchantTriggerType... overriddenBy){
        try {
            constructor = triggerClass.getConstructor(Enchantment.class, EnchantTriggerType.class);
        } catch (NoSuchMethodException e){
            constructor = null;
            Util.error("This error should not be thrown! Open an issue on the github with your enchantments.yml file!");
            Util.error("Trigger class: " + triggerClass);
        }
        this.overriddenBy = Arrays.stream(overriddenBy).collect(Collectors.toSet());
        this.conditionFunction = conditionFunction;
        this.conditionType = conditionType;
    }


    public CustomEnchantListener getTrigger(Enchantment enchantment) {
        try {
            return (CustomEnchantListener) constructor.newInstance(enchantment, this);
        } catch (Exception ignored){
            Util.error("This error should not be thrown! Open an issue on the github with your enchantments.yml file!");
            Util.error("Trigger type: " + this);
            return null;
        }
    }

    public boolean compareConditions(String condition, String value){
        int comp = 0;
        try {
            switch(conditionType){
                case DOUBLE:
                    Double conditionDouble = Double.valueOf(condition);
                    Double valueDouble = Double.valueOf(value);
                    comp = valueDouble.compareTo(conditionDouble);
                    break;
                case INTEGER:
                    Integer conditionInteger = Integer.valueOf(condition);
                    Integer valueInteger = Integer.valueOf(value);
                    comp = valueInteger.compareTo(conditionInteger);
                case STRING:
                    comp = value.compareTo(condition);
            }
        } catch(NumberFormatException e) {
            return false;
        }

        return switch (this.conditionFunction) {
            case EQUALS -> comp == 0;
            case GREATER_THAN -> comp > 0;
            case LESSER_THAN -> comp < 0;
        };
    }

    public static void fixOverrides(Map<EnchantTriggerType, Set<String>> map){
        Iterator<Map.Entry<EnchantTriggerType, Set<String>>> it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<EnchantTriggerType, Set<String>> entry = it.next();
            EnchantTriggerType type = entry.getKey();
            type.overriddenBy.forEach(overriddenByType -> {
                if (map.containsKey(overriddenByType)) {
                    map.get(overriddenByType).addAll(entry.getValue());
                    it.remove();
                    Util.error(type + " was overridden by " + overriddenByType);
                }
            });
        }
    }

    enum ConditionType {
        STRING,
        DOUBLE,
        INTEGER

    }

    enum ConditionFunction {
        EQUALS,
        GREATER_THAN,
        LESSER_THAN


    }
}
