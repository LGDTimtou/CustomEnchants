package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.armor.ArmorDeEquipTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.armor.ArmorEquipTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.projectiles.*;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block.*;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block_other.ActivateSculkSensorTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block_other.BellRungTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block_other.ChangeSignTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block_other.PrimeTNTTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.click.LeftClickItemTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.click.RightClickItemTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.damage.DamageAnimalTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.damage.DamageEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.damage.DamageMobTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.damage.DamagePlayerTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.fishing_rod.FishingRodCaughtTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.fishing_rod.FishingRodHitPlayerTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.health.PlayerHealthChangeTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.health.PlayerHealthDecreaseTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.health.PlayerHealthIncreaseTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill.KillAnimalTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill.KillEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill.KillMobTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill.KillPlayerTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.take_damage.TakeDamageFromEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.take_damage.TakeDamageFromMobTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.take_damage.TakeDamageFromNonEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.take_damage.TakeDamageFromPlayerTrigger;
import com.lgdtimtou.customenchantments.other.Util;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum EnchantTriggerType {

    //Projectiles
    PROJECTILE_LAND(ProjectileLandTrigger.class),
    PROJECTILE_HIT_BLOCK(ProjectileHitBlockTrigger.class, PROJECTILE_LAND),
    PROJECTILE_HIT_ENTITY(ProjectileHitEntityTrigger.class, PROJECTILE_LAND),
    ARROW_LAND(ArrowLandTrigger.class, PROJECTILE_HIT_ENTITY),
    ARROW_HIT_BLOCK(ArrowHitBlockTrigger.class, ARROW_LAND),
    ARROW_HIT_ENTITY(ArrowHitEntityTrigger.class, ARROW_LAND),

    SNOWBALL_LAND(SnowballLandTrigger.class, PROJECTILE_HIT_ENTITY),
    SNOWBALL_HIT_BLOCK(SnowballHitBlockTrigger.class, SNOWBALL_LAND),
    SNOWBALL_HIT_ENTITY(SnowballHitEntityTrigger.class, SNOWBALL_LAND),

    //Armor
    EQUIP_ARMOR(ArmorEquipTrigger.class),
    DE_EQUIP_ARMOR(ArmorDeEquipTrigger.class),

    //Other
    PRIME_TNT(PrimeTNTTrigger.class),
    CHANGE_SIGN(ChangeSignTrigger.class),
    BELL_RUNG(BellRungTrigger.class),
    SCULK_SENSOR_ACTIVATED(ActivateSculkSensorTrigger.class),

    //Blocks
    IGNITE_BLOCK(BlockIgniteTrigger.class),
    FERTILIZE_BLOCK(BlockFertilizeTrigger.class),
    BREAK_BLOCK(BreakBlockTrigger.class),
    PLACE_BLOCK(BlockPlaceTrigger.class),
    DAMAGE_BLOCK(BlockDamagedTrigger.class, BREAK_BLOCK),

    //Kill
    KILL_ENTITY(KillEntityTrigger.class),
    KILL_MOB(KillMobTrigger.class, KILL_ENTITY),
    KILL_ANIMAL(KillAnimalTrigger.class, KILL_ENTITY),
    KILL_PLAYER(KillPlayerTrigger.class, KILL_ENTITY),

    //Damage
    DAMAGE_ENTITY(DamageEntityTrigger.class),
    DAMAGE_MOB(DamageMobTrigger.class, DAMAGE_ENTITY),
    DAMAGE_ANIMAL(DamageAnimalTrigger.class, DAMAGE_ENTITY),
    DAMAGE_PLAYER(DamagePlayerTrigger.class, DAMAGE_ENTITY),

    //Take damage
    TAKE_DAMAGE_FROM_NONENTITY(TakeDamageFromNonEntityTrigger.class),
    TAKE_DAMAGE_FROM_ENTITY(TakeDamageFromEntityTrigger.class),
    TAKE_DAMAGE_FROM_PLAYER(TakeDamageFromPlayerTrigger.class, TAKE_DAMAGE_FROM_ENTITY),
    TAKE_DAMAGE_FROM_MOB(TakeDamageFromMobTrigger.class, TAKE_DAMAGE_FROM_ENTITY),

    //Fishing
    FISHING_ROD_CAUGHT(FishingRodCaughtTrigger.class),
    FISHING_ROD_HIT_PLAYER(FishingRodHitPlayerTrigger.class),

    //Health
    HEALTH_CHANGE(PlayerHealthChangeTrigger.class),
    HEALTH_CHANGE_GREATER_THAN(PlayerHealthChangeTrigger.class, ConditionFunction.GREATER_THAN, ConditionType.DOUBLE, HEALTH_CHANGE),
    HEALTH_CHANGE_LESSER_THAN(PlayerHealthChangeTrigger.class, ConditionFunction.LESSER_THAN, ConditionType.DOUBLE, HEALTH_CHANGE),
    HEALTH_INCREASE(PlayerHealthIncreaseTrigger.class, HEALTH_CHANGE),
    HEALTH_INCREASE_GREATER_THAN(PlayerHealthIncreaseTrigger.class, ConditionFunction.GREATER_THAN, ConditionType.DOUBLE, HEALTH_INCREASE),
    HEALTH_INCREASE_LESSER_THAN(PlayerHealthIncreaseTrigger.class, ConditionFunction.LESSER_THAN, ConditionType.DOUBLE, HEALTH_INCREASE),
    HEALTH_DECREASE(PlayerHealthDecreaseTrigger.class, HEALTH_CHANGE),
    HEALTH_DECREASE_GREATER_THAN(PlayerHealthDecreaseTrigger.class, ConditionFunction.GREATER_THAN, ConditionType.DOUBLE, HEALTH_DECREASE),
    HEALTH_DECREASE_LESSER_THAN(PlayerHealthDecreaseTrigger.class, ConditionFunction.LESSER_THAN, ConditionType.DOUBLE, HEALTH_DECREASE),

    //Click
    LEFT_CLICK_ITEM(LeftClickItemTrigger.class),
    RIGHT_CLICK_ITEM(RightClickItemTrigger.class);

    private final Set<EnchantTriggerType> overriddenBy;
    private Constructor<?> constructor;
    private final ConditionFunction conditionFunction;
    private final ConditionType conditionType;

    EnchantTriggerType (Class<?> triggerClass, EnchantTriggerType... overriddenBy){
        this(triggerClass, ConditionFunction.EQUALS, ConditionType.STRING, overriddenBy);
    }

    EnchantTriggerType(Class<?> triggerClass, ConditionFunction conditionFunction, ConditionType conditionType, EnchantTriggerType... overriddenBy){
        try {
            constructor = triggerClass.getConstructor(CustomEnchant.class, EnchantTriggerType.class);
        } catch (NoSuchMethodException e){
            constructor = null;
            Util.error("This error should not be thrown! Open an issue on the github with your enchantments.yml file!");
            Util.error("Trigger class: " + triggerClass);
        }
        this.overriddenBy = Arrays.stream(overriddenBy).collect(Collectors.toSet());
        this.overriddenBy.forEach(type -> type.addOverridenBy(this.overriddenBy));
        this.conditionFunction = conditionFunction;
        this.conditionType = conditionType;
    }

    public Set<EnchantTriggerType> addOverridenBy(Set<EnchantTriggerType> set){
        this.overriddenBy.forEach(type -> {
            type.addOverridenBy(set);
            set.add(type);
        });
        return set;
    }


    public CustomEnchantListener getTrigger(CustomEnchant customEnchant) {
        try {
            return (CustomEnchantListener) constructor.newInstance(customEnchant, this);
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
                    Pattern pattern = Pattern.compile(condition.toLowerCase().replace("*", ".*"));
                    return pattern.matcher(value.toLowerCase()).matches();
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
