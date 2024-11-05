package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.projectiles;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.EntityType;

public class ArrowLandTrigger extends ProjectileLandTrigger {
    public ArrowLandTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type, EntityType.ARROW);
    }

}
