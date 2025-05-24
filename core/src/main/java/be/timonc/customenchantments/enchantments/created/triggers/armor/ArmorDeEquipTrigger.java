package be.timonc.customenchantments.enchantments.created.triggers.armor;

import be.timonc.customenchantments.customevents.armor_equip.ArmorEquipEvent;
import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public class ArmorDeEquipTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public ArmorDeEquipTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onArmorDeEquip(ArmorEquipEvent e) {
        if (e.getOldArmorPiece() == null) return;

        triggerInvoker.trigger(
                e,
                e.getPlayer(),
                Set.of(e.getOldArmorPiece()),
                Map.of(
                        new ConditionKey(TriggerConditionType.ITEM, "new_armor"),
                        e.getNewArmorPiece() == null ? new ItemStack(Material.AIR) : e.getNewArmorPiece(),
                        new ConditionKey(TriggerConditionType.ITEM, "old_armor"),
                        e.getOldArmorPiece()
                ),
                Map.of()
        );
    }
}
