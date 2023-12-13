package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.armor;

import com.lgdtimtou.customenchants.customevents.armor_equip.ArmorEquipEvent;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;

import java.util.Map;
import java.util.Set;

public class ArmorEquipTrigger extends Trigger {
    public ArmorEquipTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e){
        executeCommands(e, e.getPlayer(), Set.of(e.getNewArmorPiece()), e.getNewArmorPiece().getType().name(), Map.of(
                "new_armor_piece", e.getNewArmorPiece().getType().name(),
                "old_armor_piece", e.getOldArmorPiece().getType().name(),
                "armor_type", e.getType().name()
        ));
    }
}
