package com.lgdtimtou.customenchantments.enchantments.created.triggers.chat;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

public class PlayerReceiveChatTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public PlayerReceiveChatTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        for (Player player : Bukkit.getOnlinePlayers())
            if (player != e.getPlayer())
                triggerType.trigger(e, player, Map.of(
                        new ConditionKey(TriggerConditionType.STRING, "message"), e.getMessage(),
                        new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "length"), e.getMessage().length(),
                        new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "length"), e.getMessage().length(),
                        new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "length"), e.getMessage().length()
                ), Map.of());
    }
}
