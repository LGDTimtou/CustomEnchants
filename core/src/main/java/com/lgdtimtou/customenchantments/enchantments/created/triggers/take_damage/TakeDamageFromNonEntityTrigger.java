package com.lgdtimtou.customenchantments.enchantments.created.triggers.take_damage;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;

public class TakeDamageFromNonEntityTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public TakeDamageFromNonEntityTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onTakeDamageFromNonEntity(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;

        triggerInvoker.trigger(
                e,
                player,
                Map.of(new ConditionKey(TriggerConditionType.CAUSE, "cause"), e.getCause()),
                Map.of()
        );
    }
}
