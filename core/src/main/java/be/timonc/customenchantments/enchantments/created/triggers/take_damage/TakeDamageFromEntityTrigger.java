package be.timonc.customenchantments.enchantments.created.triggers.take_damage;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;

public class TakeDamageFromEntityTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public TakeDamageFromEntityTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onDamageFromEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;

        Entity entity = e.getDamager();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        e.getDamager().addScoreboardTag(uniqueTag);

        triggerInvoker.trigger(e, player,
                Map.of(
                        new ConditionKey(TriggerConditionType.ENTITY, "entity"), entity,
                        new ConditionKey(TriggerConditionType.CAUSE, "damage"), e.getCause()
                ),
                Map.of("entity_tag", () -> uniqueTag),
                () -> entity.removeScoreboardTag(uniqueTag)
        );
    }
}
