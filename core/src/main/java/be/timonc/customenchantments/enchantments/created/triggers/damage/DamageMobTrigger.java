package be.timonc.customenchantments.enchantments.created.triggers.damage;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;

public class DamageMobTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public DamageMobTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Monster)) return;
        if (!(e.getDamager() instanceof Player player)) return;

        Entity entity = e.getEntity();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        entity.addScoreboardTag(uniqueTag);

        double health = entity instanceof Damageable damageable ? damageable.getHealth() : 0;
        double damage = e.getDamage();

        triggerInvoker.trigger(
                e,
                player,
                Map.of(
                        new ConditionKey(TriggerConditionType.ENTITY, "mob"), entity,
                        new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "mob_health"), health,
                        new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "mob_health"), health,
                        new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "mob_health"), health,
                        new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "damage"), damage,
                        new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "damage"), damage,
                        new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "damage"), damage
                ),
                Map.of("mob_tag", () -> uniqueTag),
                () -> entity.removeScoreboardTag(uniqueTag)
        );
    }
}

