package com.lgdtimtou.customenchantments.enchantments.created.triggers.click;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class RightClickBlockTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public RightClickBlockTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }


    @EventHandler
    public void onPlayerRightClickBlock(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) return;

        triggerInvoker.trigger(event, event.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.BLOCK, ""), event.getClickedBlock()
        ), Map.of());
    }
}
