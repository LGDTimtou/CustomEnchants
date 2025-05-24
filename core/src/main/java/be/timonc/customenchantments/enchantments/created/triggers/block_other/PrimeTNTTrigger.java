package be.timonc.customenchantments.enchantments.created.triggers.block_other;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.TNTPrimeEvent;

import java.util.Map;

public class PrimeTNTTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public PrimeTNTTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onPrimeTNT(TNTPrimeEvent e) {
        if (!(e.getPrimingEntity() instanceof Player player)) return;

        triggerInvoker.trigger(
                e,
                player,
                Map.of(new ConditionKey(TriggerConditionType.CAUSE, "prime"), e.getCause()),
                Map.of()
        );
    }
}
