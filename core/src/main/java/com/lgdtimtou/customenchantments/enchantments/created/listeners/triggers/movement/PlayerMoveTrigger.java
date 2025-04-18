package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.movement;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;

public class PlayerMoveTrigger extends Trigger {
    public PlayerMoveTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Location playerLocation = e.getPlayer().getLocation();
        executeCommands(e, e.getPlayer(), Map.ofEntries(
                Map.entry(
                        new ConditionKey(TriggerConditionType.BLOCK, "feet"),
                        playerLocation.add(0, -1, 0).getBlock()
                ),
                Map.entry(new ConditionKey(TriggerConditionType.BLOCK, ""), playerLocation.getBlock()),
                
                Map.entry(new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "from_x"), e.getFrom().getX()),
                Map.entry(new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "from_x"), e.getFrom().getX()),
                Map.entry(new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "from_x"), e.getFrom().getX()),
                Map.entry(new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "from_y"), e.getFrom().getY()),
                Map.entry(new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "from_y"), e.getFrom().getY()),
                Map.entry(new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "from_y"), e.getFrom().getY()),
                Map.entry(new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "from_z"), e.getFrom().getZ()),
                Map.entry(new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "from_z"), e.getFrom().getZ()),
                Map.entry(new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "from_z"), e.getFrom().getZ())
        ), Map.of());
    }
}
