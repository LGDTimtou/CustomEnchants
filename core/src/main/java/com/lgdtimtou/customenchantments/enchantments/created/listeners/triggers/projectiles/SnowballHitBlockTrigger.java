package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.projectiles;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.EntityType;

public class SnowballHitBlockTrigger extends ProjectileHitBlockTrigger{
    public SnowballHitBlockTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type, EntityType.SNOWBALL);
    }
}
