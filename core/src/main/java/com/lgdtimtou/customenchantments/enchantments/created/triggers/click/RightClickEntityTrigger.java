package com.lgdtimtou.customenchantments.enchantments.created.triggers.click;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Map;

public class RightClickEntityTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public RightClickEntityTrigger(TriggerType type) {
        this.triggerType = type;
    }


    @EventHandler
    public void onPlayerRightClickEntity(PlayerInteractEntityEvent event) {
        triggerType.trigger(event, event.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.ENTITY, "clicked"), event.getRightClicked()
        ), Map.of());
    }
}
