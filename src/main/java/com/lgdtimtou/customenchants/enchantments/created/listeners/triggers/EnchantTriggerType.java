package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import org.bukkit.enchantments.Enchantment;

import java.util.*;
import java.util.stream.Collectors;

public enum EnchantTriggerType {

    BREAK_BLOCK,
    KILL_ENTITY,
    KILL_MOB(KILL_ENTITY),
    KILL_ANIMAL(KILL_ENTITY),
    KILL_PLAYER(KILL_ENTITY),
    DAMAGE_ENTITY,
    DAMAGE_MOB(DAMAGE_ENTITY),
    DAMAGE_ANIMAL(DAMAGE_ENTITY),
    DAMAGE_PLAYER(DAMAGE_ENTITY),
    TAKE_DAMAGE_FROM_NONENTITY(),
    TAKE_DAMAGE_FROM_ENTITY(),
    TAKE_DAMAGE_FROM_PLAYER(TAKE_DAMAGE_FROM_ENTITY),
    TAKE_DAMAGE_FROM_MOB(TAKE_DAMAGE_FROM_ENTITY);

    private final Set<EnchantTriggerType> overriddenBy;

    EnchantTriggerType(EnchantTriggerType... overriddenBy){
        this.overriddenBy = Arrays.stream(overriddenBy).collect(Collectors.toSet());
    }

    

    public CustomEnchantListener getTrigger(Enchantment enchantment){
        return switch(this){
            case BREAK_BLOCK -> new BreakBlockTrigger(enchantment);
            case KILL_ENTITY -> new KillEntityTrigger(enchantment);
            case KILL_MOB -> new KillMobTrigger(enchantment);
            case KILL_ANIMAL -> new KillAnimalTrigger(enchantment);
            case KILL_PLAYER -> new KillPlayerTrigger(enchantment);
            case DAMAGE_ENTITY -> new DamageEntityTrigger(enchantment);
            case DAMAGE_MOB -> new DamageMobTrigger(enchantment);
            case DAMAGE_ANIMAL -> new DamageAnimalTrigger(enchantment);
            case DAMAGE_PLAYER -> new DamagePlayerTrigger(enchantment);
            case TAKE_DAMAGE_FROM_NONENTITY -> new TakeDamageFromNonEntityTrigger(enchantment);
            case TAKE_DAMAGE_FROM_ENTITY -> new TakeDamageFromEntityTrigger(enchantment);
            case TAKE_DAMAGE_FROM_MOB -> new TakeDamageFromMobTrigger(enchantment);
            case TAKE_DAMAGE_FROM_PLAYER -> new TakeDamageFromPlayerTrigger(enchantment);
        };
    }

    public Set<EnchantTriggerType> getOverriddenBy() {
        return overriddenBy;
    }

    public static void fixOverrides(ArrayList<EnchantTriggerType> list){
        Iterator<EnchantTriggerType> it = list.iterator();
        while (it.hasNext()){
            EnchantTriggerType type = it.next();
            if (list.stream().anyMatch(type.getOverriddenBy()::contains))
                it.remove();
        }
    }




}
