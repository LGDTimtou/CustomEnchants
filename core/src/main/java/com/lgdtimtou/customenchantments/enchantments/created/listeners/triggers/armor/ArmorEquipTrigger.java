package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.armor;

import com.lgdtimtou.customenchantments.customevents.armor_equip.ArmorEquipEvent;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public class ArmorEquipTrigger extends Trigger {
    public ArmorEquipTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e) {
        if (e.getNewArmorPiece() == null) return;
        executeCommands(
                e,
                e.getPlayer(),
                Set.of(e.getNewArmorPiece()),
                Map.of(
                        new ConditionKey(TriggerConditionType.ITEM, "new_armor"),
                        e.getNewArmorPiece(),
                        new ConditionKey(TriggerConditionType.ITEM, "old_armor"),
                        e.getOldArmorPiece() == null ? new ItemStack(Material.AIR) : e.getOldArmorPiece()
                ),
                Map.of()
        );
    }
}
