package be.timonc.customenchantments.enchantments.created.triggers.damage;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.Set;

public class DamagePlayerTrigger extends TriggerListener {

    private final TriggerConditionGroup damagedPlayerConditions = new TriggerConditionGroup(
            "damaged", TriggerConditionGroupType.PLAYER
    );
    private final TriggerConditionGroup damageConditions = new TriggerConditionGroup(
            "damage", TriggerConditionGroupType.NUMBER
    );
    private final TriggerConditionGroup damageCauseConditions = new TriggerConditionGroup(
            "damage", TriggerConditionGroupType.CAUSE
    );

    public DamagePlayerTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player damaged)) return;
        if (!(e.getDamager() instanceof Player player)) return;

        triggerInvoker.trigger(e, player, Map.of(
                damagedPlayerConditions, damaged,
                damageConditions, e.getDamage(),
                damageCauseConditions, e.getCause()
        ));
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(damagedPlayerConditions, damageConditions, damageCauseConditions);
    }
}
