package be.timonc.customenchantments.enchantments.created.triggers.block_other;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Arrays;
import java.util.Map;

public class ChangeSignTrigger implements TriggerListener {

    private final TriggerInvoker triggerInvoker;

    public ChangeSignTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onSignChance(SignChangeEvent e) {
        String lines = Arrays.toString(e.getLines());
        triggerInvoker.trigger(
                e,
                e.getPlayer(),
                Map.of(new ConditionKey(TriggerConditionType.STRING, "lines"), lines),
                Map.of()
        );
    }
}
