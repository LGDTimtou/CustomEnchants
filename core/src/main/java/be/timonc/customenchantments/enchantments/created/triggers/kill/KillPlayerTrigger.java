package be.timonc.customenchantments.enchantments.created.triggers.kill;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class KillPlayerTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public KillPlayerTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player killer)) return;
        if (!(e.getEntity() instanceof Player killed)) return;
        triggerInvoker.trigger(e, killer, Map.of(
                new ConditionKey(TriggerConditionType.PLAYER, "killed"), killed
        ), Map.of());
    }
}
