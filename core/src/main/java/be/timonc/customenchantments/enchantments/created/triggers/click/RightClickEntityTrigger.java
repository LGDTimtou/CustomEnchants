package be.timonc.customenchantments.enchantments.created.triggers.click;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Map;

public class RightClickEntityTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public RightClickEntityTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }


    @EventHandler
    public void onPlayerRightClickEntity(PlayerInteractEntityEvent event) {
        triggerInvoker.trigger(event, event.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.ENTITY, "clicked"), event.getRightClicked()
        ), Map.of());
    }
}
