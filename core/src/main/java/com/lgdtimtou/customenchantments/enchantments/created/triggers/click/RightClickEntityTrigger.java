package com.lgdtimtou.customenchantments.enchantments.created.triggers.click;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Map;

public class RightClickEntityTrigger implements CustomEnchantListener {

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
