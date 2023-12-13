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
    FISHING_ROD_HIT_PLAYER(FishingRodHitPlayerTrigger.class);

    private final Set<EnchantTriggerType> overriddenBy;
    private Constructor<?> constructor;

    EnchantTriggerType (Class<?> triggerClass, EnchantTriggerType... overriddenBy){
        try {
            constructor = triggerClass.getConstructor(Enchantment.class, EnchantTriggerType.class);
        } catch (NoSuchMethodException e){
            constructor = null;
            Util.error("This error should not be thrown! Open an issue on the github with your enchantments.yml file!");
            Util.error("Trigger class: " + triggerClass);
        }
        this.overriddenBy = Arrays.stream(overriddenBy).collect(Collectors.toSet());
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

    public static void fixOverrides(Map<EnchantTriggerType, Set<String>> map){
        Set<EnchantTriggerType> types = map.keySet();
        for (EnchantTriggerType type : types)
            if (types.stream().anyMatch(type.overriddenBy::contains))
                map.remove(type);
    }
}
