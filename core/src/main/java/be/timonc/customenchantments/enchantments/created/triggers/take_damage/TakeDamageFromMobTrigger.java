package be.timonc.customenchantments.enchantments.created.triggers.take_damage;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.UUID;

public class TakeDamageFromMobTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public TakeDamageFromMobTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onTakeDamageFromMob(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        if (!(e.getDamager() instanceof Monster monster))
            return;

        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        monster.addScoreboardTag(uniqueTag);

        triggerInvoker.trigger(e, player,
                Map.of(
                        new ConditionKey(TriggerConditionType.ENTITY, "mob"), monster,
                        new ConditionKey(TriggerConditionType.CAUSE, "damage"), e.getCause()
                ),
                Map.of("mob_tag", () -> uniqueTag),
                () -> monster.removeScoreboardTag(uniqueTag)
        );
    }
}
