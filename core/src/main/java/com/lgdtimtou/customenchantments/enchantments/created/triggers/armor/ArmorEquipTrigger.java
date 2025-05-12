package com.lgdtimtou.customenchantments.enchantments.created.triggers.armor;

import com.lgdtimtou.customenchantments.customevents.armor_equip.ArmorEquipEvent;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public class ArmorEquipTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public ArmorEquipTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e) {
        if (e.getNewArmorPiece() == null) return;
        triggerType.trigger(
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
