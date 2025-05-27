package be.timonc.customenchantments.enchantments.created.triggers.block_other;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BellRingEvent;

import java.util.Map;

public class BellRungTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public BellRungTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onBellRing(BellRingEvent e) {
        if (e.getEntity() == null || !(e.getEntity() instanceof Player player)) return;
        triggerInvoker.trigger(e, player, Map.of(), Map.of());
    }
}
