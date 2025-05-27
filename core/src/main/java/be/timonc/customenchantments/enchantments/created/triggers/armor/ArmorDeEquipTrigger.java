package be.timonc.customenchantments.enchantments.created.triggers.armor;

import be.timonc.customenchantments.customevents.armor_equip.ArmorEquipEvent;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerCondition;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public class ArmorDeEquipTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;
    private final TriggerCondition newArmorCondition = new TriggerCondition(TriggerConditionType.ITEM, "new_armor");
    private final TriggerCondition oldArmorCondition = new TriggerCondition(TriggerConditionType.ITEM, "old_armor");

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
                        newArmorCondition,
                        e.getNewArmorPiece() == null ? new ItemStack(Material.AIR) : e.getNewArmorPiece(),
                        oldArmorCondition,
                        e.getOldArmorPiece()
                ),
                Map.of()
        );
    }
}
