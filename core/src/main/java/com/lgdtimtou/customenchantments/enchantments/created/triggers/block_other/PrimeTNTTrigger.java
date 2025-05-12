package com.lgdtimtou.customenchantments.enchantments.created.triggers.block_other;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.TNTPrimeEvent;

import java.util.Map;

public class PrimeTNTTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public PrimeTNTTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onPrimeTNT(TNTPrimeEvent e) {
        if (!(e.getPrimingEntity() instanceof Player player)) return;

        triggerType.trigger(
                e,
                player,
                Map.of(new ConditionKey(TriggerConditionType.CAUSE, "prime"), e.getCause()),
                Map.of()
        );
    }
}
