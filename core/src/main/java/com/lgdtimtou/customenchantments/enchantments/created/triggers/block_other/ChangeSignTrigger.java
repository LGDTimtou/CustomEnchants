package com.lgdtimtou.customenchantments.enchantments.created.triggers.block_other;

import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Arrays;
import java.util.Map;

public class ChangeSignTrigger implements CustomEnchantListener {

    private final TriggerType triggerType;

    public ChangeSignTrigger(TriggerType type) {
        this.triggerType = type;
    }

    @EventHandler
    public void onSignChance(SignChangeEvent e) {
        String lines = Arrays.toString(e.getLines());
        triggerType.trigger(
                e,
                e.getPlayer(),
                Map.of(new ConditionKey(TriggerConditionType.STRING, "lines"), lines),
                Map.of()
        );
    }
}
