package com.lgdtimtou.customenchantments.enchantments.created.triggers.block;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFertilizeEvent;

import java.util.Map;

public class BlockFertilizeTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public BlockFertilizeTrigger(TriggerType type) {
        this.triggerType = type;
    }


    @EventHandler
    public void onFertilize(BlockFertilizeEvent e) {
        triggerType.trigger(
                e,
                e.getPlayer(),
                Map.of(new ConditionKey(TriggerConditionType.BLOCK, ""), e.getBlock()),
                Map.of()
        );
    }
}
