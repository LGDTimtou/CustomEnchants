package be.timonc.customenchantments.enchantments.created.triggers.kill;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.UUID;

public class KillMobTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public KillMobTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }


    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player killer)) return;
        if (!(e.getEntity() instanceof Monster))
            return;

        Entity entity = e.getEntity();
        String uniqueTag = "entity_" + UUID.randomUUID().toString().substring(0, 8);
        entity.addScoreboardTag(uniqueTag);
        triggerInvoker.trigger(
                e,
                killer,
                Map.of(new ConditionKey(TriggerConditionType.ENTITY, "mob"), entity),
                Map.of("mob_tag", () -> uniqueTag),
                () -> entity.removeScoreboardTag(uniqueTag)
        );
    }
}
