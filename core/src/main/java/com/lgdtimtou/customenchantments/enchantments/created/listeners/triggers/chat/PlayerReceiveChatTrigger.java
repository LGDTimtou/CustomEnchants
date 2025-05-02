package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.chat;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

public class PlayerReceiveChatTrigger extends Trigger {
    public PlayerReceiveChatTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        for (Player player : Bukkit.getOnlinePlayers())
            if (player != e.getPlayer())
                executeCommands(e, player, Map.of(
                        new ConditionKey(TriggerConditionType.STRING, "message"), e.getMessage(),
                        new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "length"), e.getMessage().length(),
                        new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "length"), e.getMessage().length(),
                        new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "length"), e.getMessage().length()
                ), Map.of());
    }
}
