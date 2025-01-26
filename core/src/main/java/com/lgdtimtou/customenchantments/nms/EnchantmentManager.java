package com.lgdtimtou.customenchantments.nms;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchantRecord;
import org.bukkit.enchantments.Enchantment;

import java.util.Set;

public interface EnchantmentManager {

    void freezeRegistry();

    void unFreezeRegistry();

    Enchantment registerEnchantment(CustomEnchantRecord customEnchant);

    void addExclusives(String enchantId, Set<String> exclusives);

}
