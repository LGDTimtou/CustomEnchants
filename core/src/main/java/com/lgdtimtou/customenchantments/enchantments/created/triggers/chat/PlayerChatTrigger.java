package com.lgdtimtou.customenchantments.enchantments.created.triggers.chat;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

public class PlayerChatTrigger implements CustomEnchantListener {

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
