package be.timonc.customenchantments.enchantments.custom.triggers.armor;

import be.timonc.customenchantments.customevents.armor_equip.ArmorEquipEvent;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public class ArmorDeEquipTrigger extends TriggerListener {

    private final TriggerConditionGroup newArmorConditions = new TriggerConditionGroup(
            "new_armor", TriggerConditionGroupType.ITEM
    );
    private final TriggerConditionGroup oldArmorConditions = new TriggerConditionGroup(
            "old_armor", TriggerConditionGroupType.ITEM
    );

    public ArmorDeEquipTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onArmorDeEquip(ArmorEquipEvent e) {
        if (e.getOldArmorPiece() == null) return;

        triggerInvoker.trigger(
                e,
                e.getPlayer(),
                Set.of(e.getOldArmorPiece()),
                Map.of(
                        newArmorConditions,
                        e.getNewArmorPiece() == null ? new ItemStack(Material.AIR) : e.getNewArmorPiece(),
                        oldArmorConditions,
                        e.getOldArmorPiece()
                ),
                Map.of()
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(newArmorConditions, oldArmorConditions);
    }
}
