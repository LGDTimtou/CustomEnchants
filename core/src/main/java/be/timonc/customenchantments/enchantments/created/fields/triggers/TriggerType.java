package be.timonc.customenchantments.enchantments.created.fields.triggers;

import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerCondition;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import be.timonc.customenchantments.enchantments.created.triggers.armor.ArmorDeEquipTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.armor.ArmorEquipTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.block.*;
import be.timonc.customenchantments.enchantments.created.triggers.block_other.ActivateSculkSensorTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.block_other.BellRungTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.block_other.ChangeSignTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.block_other.PrimeTNTTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.chat.PlayerChatTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.chat.PlayerReceiveChatTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.click.*;
import be.timonc.customenchantments.enchantments.created.triggers.damage.DamageAnimalTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.damage.DamageEntityTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.damage.DamageMobTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.damage.DamagePlayerTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.fishing_rod.FishingRodCaughtTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.fishing_rod.FishingRodHitEntityTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.fishing_rod.FishingRodHitPlayerTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.health_change.*;
import be.timonc.customenchantments.enchantments.created.triggers.inventory.InventoryCloseTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.kill.KillAnimalTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.kill.KillEntityTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.kill.KillMobTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.kill.KillPlayerTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.movement.*;
import be.timonc.customenchantments.enchantments.created.triggers.projectiles.ProjectileHitBlockTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.projectiles.ProjectileHitEntityTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.projectiles.ProjectileHitPlayerTrigger;
import be.timonc.customenchantments.enchantments.created.triggers.projectiles.ProjectileLandTrigger;
import be.timonc.customenchantments.other.Util;

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

    //Health Change
    HEALTH_CHANGE(HealthChangeTrigger.class),
    REGAIN_HEALTH(RegainHealthTrigger.class, HEALTH_CHANGE),
    TAKE_DAMAGE(TakeDamageTrigger.class, HEALTH_CHANGE),
    TAKE_DAMAGE_FROM_NON_ENTITY(TakeDamageFromNonEntityTrigger.class, TAKE_DAMAGE),
    TAKE_DAMAGE_FROM_ENTITY(TakeDamageFromEntityTrigger.class, TAKE_DAMAGE),
    TAKE_DAMAGE_FROM_MOB(TakeDamageFromMobTrigger.class, TAKE_DAMAGE_FROM_ENTITY),
    TAKE_DAMAGE_FROM_PLAYER(TakeDamageFromPlayerTrigger.class, TAKE_DAMAGE_FROM_ENTITY),

    //Fishing
    FISHING_ROD_CAUGHT(FishingRodCaughtTrigger.class),
    FISHING_ROD_HIT_ENTITY(FishingRodHitEntityTrigger.class),
    FISHING_ROD_HIT_PLAYER(FishingRodHitPlayerTrigger.class, FISHING_ROD_HIT_ENTITY),

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
    PLAYER_MOVE(PlayerMoveTrigger.class),
    PLAYER_SWIM(PlayerSwimTrigger.class, PLAYER_MOVE),

    PLAYER_SNEAK_TOGGLE(PlayerSneakToggleTrigger.class),
    PLAYER_SNEAK_DOWN(PlayerSneakDownTrigger.class, PLAYER_SNEAK_TOGGLE),
    PLAYER_SNEAK_UP(PlayerSneakUpTrigger.class, PLAYER_SNEAK_TOGGLE),

    //Projectiles
    PROJECTILE_LAND(ProjectileLandTrigger.class),
    PROJECTILE_HIT_BLOCK(ProjectileHitBlockTrigger.class, PROJECTILE_LAND),
    PROJECTILE_HIT_ENTITY(ProjectileHitEntityTrigger.class, PROJECTILE_LAND),
    PROJECTILE_HIT_PLAYER(ProjectileHitPlayerTrigger.class, PROJECTILE_HIT_ENTITY);

    private final Set<TriggerType> overriddenBy;
    private TriggerListener instance;
    private TriggerInvoker invoker;
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

    public Set<TriggerType> getOverriddenBy() {
        return overriddenBy;
    }

    private void addOverriddenBy(Set<TriggerType> set) {
        this.overriddenBy.forEach(type -> {
            type.addOverriddenBy(set);
            set.add(type);
        });
    }

    public TriggerInvoker getInvoker() {
        if (invoker == null)
            invoker = new TriggerInvoker();
        return invoker;
    }

    public TriggerListener getOrCreateInstance() {
        try {
            if (instance == null) {
                instance = (TriggerListener) constructor.newInstance(getInvoker());
                Util.registerListener(instance);
            }
        } catch (Exception ignored) {
            Util.error("Trigger type: " + this + " could not be instanced! Please report this error!");
        }
        return instance;
    }

    public TriggerCondition getTriggerCondition(TriggerConditionGroupType type, String name) {
        return getOrCreateInstance().getConditions().stream()
                                    .filter(triggerCondition -> triggerCondition.group() == type && triggerCondition.name()
                                                                                                                    .equals(name))
                                    .findFirst()
                                    .orElse(null);
    }
}
