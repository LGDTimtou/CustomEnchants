package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block.*;
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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public enum EnchantTriggerType {

    BELL_RUNG(),
    FERTILIZE_BLOCK(),
    BREAK_BLOCK(),
    PLACE_BLOCK(),
    DAMAGE_BLOCK(BREAK_BLOCK),

    KILL_ENTITY(),
    KILL_MOB(KILL_ENTITY),
    KILL_ANIMAL(KILL_ENTITY),
    KILL_PLAYER(KILL_ENTITY),

    DAMAGE_ENTITY(),
    DAMAGE_MOB(DAMAGE_ENTITY),
    DAMAGE_ANIMAL(DAMAGE_ENTITY),
    DAMAGE_PLAYER(DAMAGE_ENTITY),

    TAKE_DAMAGE_FROM_NONENTITY(),
    TAKE_DAMAGE_FROM_ENTITY(),
    TAKE_DAMAGE_FROM_PLAYER(TAKE_DAMAGE_FROM_ENTITY),
    TAKE_DAMAGE_FROM_MOB(TAKE_DAMAGE_FROM_ENTITY),

    FISHING_ROD_CAUGHT(),
    FISHING_ROD_HIT_PLAYER();

    private final Set<EnchantTriggerType> overriddenBy;

    EnchantTriggerType (EnchantTriggerType... overriddenBy){
        this.overriddenBy = Arrays.stream(overriddenBy).collect(Collectors.toSet());
    }

    public CustomEnchantListener getTrigger(Enchantment enchantment){
        return switch(this){
            case BELL_RUNG -> new BellRungTrigger(enchantment);
            case FERTILIZE_BLOCK -> new BlockFertilizeTrigger(enchantment);
            case BREAK_BLOCK -> new BreakBlockTrigger(enchantment);
            case PLACE_BLOCK -> new BlockPlaceTrigger(enchantment);
            case DAMAGE_BLOCK -> new BlockDamagedTrigger(enchantment);

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

            case FISHING_ROD_CAUGHT -> new FishingRodCaughtTrigger(enchantment);
            case FISHING_ROD_HIT_PLAYER -> new FishingRodHitPlayerTrigger(enchantment);
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
