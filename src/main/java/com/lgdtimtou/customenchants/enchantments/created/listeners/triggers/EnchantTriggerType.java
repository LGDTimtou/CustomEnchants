package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

public enum EnchantTriggerType {

    BLOCK_BREAK,
    MOB_KILL,
    ANIMAL_KILL,
    PLAYER_KILL;

    public CustomEnchantListener getTrigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels){
        return switch(this){
            case BLOCK_BREAK -> new BlockBreakTrigger(enchantment, levels);
            case MOB_KILL -> new MobKillTrigger(enchantment, levels);
            case ANIMAL_KILL -> new AnimalKillTrigger(enchantment, levels);
            case PLAYER_KILL -> new PlayerKillTrigger(enchantment, levels);
        };
        
        
    }

}
