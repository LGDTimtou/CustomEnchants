package be.timonc.customenchantments.enchantments.created.triggers.chat;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

public class PlayerReceiveChatTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public PlayerReceiveChatTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        for (Player player : Bukkit.getOnlinePlayers())
            if (player != e.getPlayer())
                triggerInvoker.trigger(e, player, Map.of(
                        new ConditionKey(TriggerConditionType.STRING, "message"), e.getMessage(),
                        new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "length"), e.getMessage().length(),
                        new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "length"), e.getMessage().length(),
                        new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "length"), e.getMessage().length()
                ), Map.of());
    }
}
