package be.timonc.customenchantments.enchantments.created.triggers.kill;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.UUID;

public class KillAnimalTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public KillAnimalTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onAnimalKill(EntityDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player killer)) return;
        if (!(e.getEntity() instanceof Animals))
            return;

        Entity entity = e.getEntity();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        entity.addScoreboardTag(uniqueTag);
        triggerInvoker.trigger(
                e,
                killer,
                Map.of(new ConditionKey(TriggerConditionType.ENTITY, "animal"), entity),
                Map.of("animal_tag", () -> uniqueTag),
                () -> entity.removeScoreboardTag(uniqueTag)
        );
    }
}
