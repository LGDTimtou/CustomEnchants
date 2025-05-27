package be.timonc.customenchantments.enchantments.created.triggers.chat;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

public class PlayerChatTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public PlayerChatTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent e) {
        triggerInvoker.trigger(e, e.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.STRING, "message"), e.getMessage(),
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "length"), e.getMessage().length(),
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "length"), e.getMessage().length(),
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "length"), e.getMessage().length()
        ), Map.of());
    }
}
