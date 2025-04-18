package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.armor.ArmorDeEquipTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.armor.ArmorEquipTrigger;
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
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.fishing_rod.FishingRodHitEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.fishing_rod.FishingRodHitPlayerTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.health.PlayerHealthChangeTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.health.PlayerHealthDecreaseTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.health.PlayerHealthIncreaseTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.inventory.InventoryCloseTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill.KillAnimalTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill.KillEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill.KillMobTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.kill.KillPlayerTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.movement.PlayerMoveTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.movement.PlayerSneakTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.projectiles.ProjectileHitBlockTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.projectiles.ProjectileHitEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.projectiles.ProjectileLandTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.take_damage.TakeDamageFromEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.take_damage.TakeDamageFromMobTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.take_damage.TakeDamageFromNonEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.take_damage.TakeDamageFromPlayerTrigger;
import com.lgdtimtou.customenchantments.other.Util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum EnchantTriggerType {

    //Projectiles
    PROJECTILE_LAND(ProjectileLandTrigger.class),
    PROJECTILE_HIT_BLOCK(ProjectileHitBlockTrigger.class, PROJECTILE_LAND),
    PROJECTILE_HIT_ENTITY(ProjectileHitEntityTrigger.class, PROJECTILE_LAND),

    //Armor
    ARMOR_EQUIP(ArmorEquipTrigger.class),
    ARMOR_DE_EQUIP(ArmorDeEquipTrigger.class),

    //Other
    PRIME_TNT(PrimeTNTTrigger.class),
    CHANGE_SIGN(ChangeSignTrigger.class),
    BELL_RUNG(BellRungTrigger.class),
    ACTIVATE_SCULK_SENSOR(ActivateSculkSensorTrigger.class),
    INVENTORY_CLOSE(InventoryCloseTrigger.class),

    //Blocks
    BLOCK_IGNITE(BlockIgniteTrigger.class),
    BLOCK_FERTILIZE(BlockFertilizeTrigger.class),
    BLOCK_BREAK(BlockBreakTrigger.class),
    BLOCK_PLACE(BlockPlaceTrigger.class),
    BLOCK_DAMAGED(BlockDamagedTrigger.class, BLOCK_BREAK),

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
    TAKE_DAMAGE_FROM_MOB(TakeDamageFromMobTrigger.class, TAKE_DAMAGE_FROM_ENTITY),
    TAKE_DAMAGE_FROM_PLAYER(TakeDamageFromPlayerTrigger.class, TAKE_DAMAGE_FROM_ENTITY),

    //Fishing
    FISHING_ROD_CAUGHT(FishingRodCaughtTrigger.class),
    FISHING_ROD_HIT_ENTITY(FishingRodHitEntityTrigger.class),
    FISHING_ROD_HIT_PLAYER(FishingRodHitPlayerTrigger.class, FISHING_ROD_HIT_ENTITY),

    //Health
    HEALTH_CHANGE(PlayerHealthChangeTrigger.class),
    HEALTH_INCREASE(PlayerHealthIncreaseTrigger.class, HEALTH_CHANGE),
    HEALTH_DECREASE(PlayerHealthDecreaseTrigger.class, HEALTH_CHANGE),

    //Click
    LEFT_CLICK_ITEM(LeftClickItemTrigger.class),
    RIGHT_CLICK_ITEM(RightClickItemTrigger.class),

    //Movement
    PLAYER_SNEAK(PlayerSneakTrigger.class),
    PLAYER_MOVE(PlayerMoveTrigger.class);

    private final Set<EnchantTriggerType> overriddenBy;
    private Constructor<?> constructor;

    EnchantTriggerType(Class<?> triggerClass, EnchantTriggerType... overriddenBy) {
        try {
            constructor = triggerClass.getConstructor(CustomEnchant.class, EnchantTriggerType.class);
        } catch (NoSuchMethodException e) {
            constructor = null;
            Util.error("This error should not be thrown! Open an issue on the github with your enchantments.yml file!");
            Util.error("Trigger class: " + triggerClass);
        }
        this.overriddenBy = Arrays.stream(overriddenBy).collect(Collectors.toSet());
        this.overriddenBy.forEach(type -> type.addOverriddenBy(this.overriddenBy));
    }

    public Set<EnchantTriggerType> getOverriddenBy() {
        return overriddenBy;
    }

    public void addOverriddenBy(Set<EnchantTriggerType> set) {
        this.overriddenBy.forEach(type -> {
            type.addOverriddenBy(set);
            set.add(type);
        });
    }


    public CustomEnchantListener getTrigger(CustomEnchant customEnchant) {
        try {
            return (CustomEnchantListener) constructor.newInstance(customEnchant, this);
        } catch (Exception ignored) {
            Util.error("This error should not be thrown! Open an issue on the github with your enchantments.yml file!");
            Util.error("Trigger type: " + this);
            return null;
        }
    }
}
