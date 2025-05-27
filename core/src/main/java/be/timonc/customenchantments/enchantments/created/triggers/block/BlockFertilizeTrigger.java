package be.timonc.customenchantments.enchantments.created.triggers.block;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFertilizeEvent;

import java.util.Map;

public class BlockFertilizeTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public BlockFertilizeTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }


    @EventHandler
    public void onFertilize(BlockFertilizeEvent e) {
        triggerInvoker.trigger(
                e,
                e.getPlayer(),
                Map.of(new ConditionKey(TriggerConditionType.BLOCK, ""), e.getBlock()),
                Map.of()
        );
    }
}
