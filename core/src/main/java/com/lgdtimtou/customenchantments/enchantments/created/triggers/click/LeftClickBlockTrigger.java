package com.lgdtimtou.customenchantments.enchantments.created.triggers.click;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class LeftClickBlockTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public LeftClickBlockTrigger(TriggerType type) {
        this.triggerType = type;
    }


    @EventHandler
    public void onPlayerLeftClickBlock(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.getClickedBlock() == null) return;

        triggerType.trigger(event, event.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.BLOCK, ""), event.getClickedBlock()
        ), Map.of());
    }
}
