package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.armor;

import com.lgdtimtou.customenchantments.customevents.armor_equip.ArmorEquipEvent;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.event.EventHandler;

import java.util.Map;
import java.util.Set;

public class ArmorEquipTrigger extends Trigger {
    public ArmorEquipTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e) {
        String armorType = e.getType().name();
        executeCommands(
                e,
                e.getPlayer(),
                Set.of(e.getNewArmorPiece()),
                Map.of(
                        new ConditionKey(TriggerConditionType.ITEM, "new_armor"),
                        e.getNewArmorPiece(),
                        new ConditionKey(TriggerConditionType.ITEM, "old_armor"),
                        e.getOldArmorPiece()
                ),
                Map.of("armor_type", () -> armorType)
        );
    }
}
