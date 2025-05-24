package com.lgdtimtou.customenchantments.enchantments.created.fields.triggers;

import com.lgdtimtou.customenchantments.enchantments.created.triggers.armor.ArmorDeEquipTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.armor.ArmorEquipTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.block.*;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.block_other.ActivateSculkSensorTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.block_other.BellRungTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.block_other.ChangeSignTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.block_other.PrimeTNTTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.chat.PlayerChatTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.chat.PlayerReceiveChatTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.click.*;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.damage.DamageAnimalTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.damage.DamageEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.damage.DamageMobTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.damage.DamagePlayerTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.fishing_rod.FishingRodCaughtTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.fishing_rod.FishingRodHitEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.fishing_rod.FishingRodHitPlayerTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.health.PlayerHealthChangeTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.health.PlayerHealthDecreaseTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.health.PlayerHealthIncreaseTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.inventory.InventoryCloseTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.kill.KillAnimalTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.kill.KillEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.kill.KillMobTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.kill.KillPlayerTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.movement.PlayerIdleTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.movement.PlayerMoveTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.movement.PlayerSneakTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.movement.PlayerSwimTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.projectiles.ProjectileHitBlockTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.projectiles.ProjectileHitEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.projectiles.ProjectileHitPlayerTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.projectiles.ProjectileLandTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.take_damage.TakeDamageFromEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.take_damage.TakeDamageFromMobTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.take_damage.TakeDamageFromNonEntityTrigger;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.take_damage.TakeDamageFromPlayerTrigger;
import com.lgdtimtou.customenchantments.other.Util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum TriggerType {

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

    //Chat
    PLAYER_CHAT(PlayerChatTrigger.class),
    PLAYER_RECEIVE_CHAT(PlayerReceiveChatTrigger.class),

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
    LEFT_CLICK(LeftClickTrigger.class),
    LEFT_CLICK_AIR(LeftClickAirTrigger.class, LEFT_CLICK),
    LEFT_CLICK_BLOCK(LeftClickBlockTrigger.class, LEFT_CLICK),
    RIGHT_CLICK(RightClickTrigger.class),
    RIGHT_CLICK_AIR(RightClickAirTrigger.class, RIGHT_CLICK),
    RIGHT_CLICK_BLOCK(RightClickBlockTrigger.class, RIGHT_CLICK),
    RIGHT_CLICK_ENTITY(RightClickEntityTrigger.class, RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK),

    //Movement
    PLAYER_IDLE(PlayerIdleTrigger.class),
    PLAYER_SNEAK(PlayerSneakTrigger.class),
    PLAYER_MOVE(PlayerMoveTrigger.class),
    PLAYER_SWIM(PlayerSwimTrigger.class, PLAYER_MOVE),


    //Projectiles
    PROJECTILE_LAND(ProjectileLandTrigger.class),
    PROJECTILE_HIT_BLOCK(ProjectileHitBlockTrigger.class, PROJECTILE_LAND),
    PROJECTILE_HIT_ENTITY(ProjectileHitEntityTrigger.class, PROJECTILE_LAND),
    PROJECTILE_HIT_PLAYER(ProjectileHitPlayerTrigger.class, PROJECTILE_HIT_ENTITY);

    private final Set<TriggerType> overriddenBy;
    private Constructor<?> constructor;

    TriggerType(Class<?> triggerClass, TriggerType... overriddenBy) {
        try {
            constructor = triggerClass.getConstructor(TriggerInvoker.class);
        } catch (NoSuchMethodException e) {
            constructor = null;
            Util.error("Could not find constructor for " + this + ", please report this error!");
        }

        this.overriddenBy = new HashSet<>(Arrays.asList(overriddenBy));
        Set<TriggerType> tempSet = new HashSet<>(this.overriddenBy);
        tempSet.forEach(type -> type.addOverriddenBy(this.overriddenBy));
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }


    public Set<TriggerType> getOverriddenBy() {
        return overriddenBy;
    }

    private void addOverriddenBy(Set<TriggerType> set) {
        this.overriddenBy.forEach(type -> {
            type.addOverriddenBy(set);
            set.add(type);
        });
    }
}
