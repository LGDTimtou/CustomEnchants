package be.timonc.customenchantments.nms;

import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.enchantments.CustomEnchantRecord;
import org.bukkit.enchantments.Enchantment;

import java.util.Set;

public interface EnchantmentManager {

    void freezeRegistry();

    void unFreezeRegistry();

    Enchantment registerEnchantment(CustomEnchantRecord customEnchant);

    void addExclusives(String enchantId, Set<String> exclusives);

    void addTagsOnReload(CustomEnchant customEnchant);
}
