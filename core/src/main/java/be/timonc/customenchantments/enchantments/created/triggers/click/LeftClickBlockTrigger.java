package be.timonc.customenchantments.enchantments.created.triggers.click;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class LeftClickBlockTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public LeftClickBlockTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }


    @EventHandler
    public void onPlayerLeftClickBlock(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.getClickedBlock() == null) return;

        triggerInvoker.trigger(event, event.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.BLOCK, ""), event.getClickedBlock()
        ), Map.of());
    }
}
