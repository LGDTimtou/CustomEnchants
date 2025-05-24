package be.timonc.customenchantments.enchantments.created.triggers.click;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class RightClickTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public RightClickTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        triggerInvoker.trigger(e, e.getPlayer(), Map.of(), Map.of());
    }
}

