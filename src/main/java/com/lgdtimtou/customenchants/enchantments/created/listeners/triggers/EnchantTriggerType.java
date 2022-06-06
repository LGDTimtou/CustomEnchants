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
    DAMAGE_PLAYER(DAMAGE_ENTITY);

    private final Set<EnchantTriggerType> overriddenBy;

    EnchantTriggerType(EnchantTriggerType... overriddenBy){
        this.overriddenBy = Arrays.stream(overriddenBy).collect(Collectors.toSet());
    }

    

    public CustomEnchantListener getTrigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels){
        return switch(this){
            case BREAK_BLOCK -> new BreakBlockTrigger(enchantment, levels);
            case KILL_ENTITY -> new KillEntityTrigger(enchantment, levels);
            case KILL_MOB -> new KillMobTrigger(enchantment, levels);
            case KILL_ANIMAL -> new KillAnimalTrigger(enchantment, levels);
            case KILL_PLAYER -> new KillPlayerTrigger(enchantment, levels);
            case DAMAGE_ENTITY -> new DamageEntityTrigger(enchantment, levels);
            case DAMAGE_MOB -> new DamageMobTrigger(enchantment, levels);
            case DAMAGE_ANIMAL -> new DamageAnimalTrigger(enchantment, levels);
            case DAMAGE_PLAYER -> new DamagePlayerTrigger(enchantment, levels);
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
