package be.timonc.customenchantments.enchantments.created.triggers.click;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class LeftClickAirTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public LeftClickAirTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }


    @EventHandler
    public void onPlayerLeftClickAir(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR) return;

        triggerInvoker.trigger(event, event.getPlayer(), Map.of(), Map.of());
    }
}
