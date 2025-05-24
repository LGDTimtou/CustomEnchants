package be.timonc.customenchantments.enchantments.created.triggers.damage;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class DamagePlayerTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public DamagePlayerTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player damaged)) return;
        if (!(e.getDamager() instanceof Player player)) return;

        double health = damaged.getHealth();
        double damage = e.getDamage();

        triggerInvoker.trigger(e, player, Map.of(
                new ConditionKey(TriggerConditionType.PLAYER, "damaged"), damaged,
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "damaged_health"), health,
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "damaged_health"), health,
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "damaged_health"), health,
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "damage"), damage,
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "damage"), damage,
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "damage"), damage
        ), Map.of());
    }
}
