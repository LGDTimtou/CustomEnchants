package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.armor;

import com.lgdtimtou.customenchantments.customevents.armor_equip.ArmorEquipEvent;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.event.EventHandler;

import java.util.Map;
import java.util.Set;

public class ArmorDeEquipTrigger extends Trigger {
    public ArmorDeEquipTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onArmorDeEquip(ArmorEquipEvent e){
        executeCommands(e, e.getPlayer(), Set.of(e.getOldArmorPiece()), e.getNewArmorPiece().getType().name(), Map.of(
                "new_armor_piece", e.getNewArmorPiece().getType().name(),
                "old_armor_piece", e.getOldArmorPiece().getType().name(),
                "armor_type", e.getType().name()
        ));
    }

}
