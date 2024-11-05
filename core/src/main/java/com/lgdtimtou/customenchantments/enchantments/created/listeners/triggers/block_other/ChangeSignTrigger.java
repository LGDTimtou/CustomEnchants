package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.block_other;

import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Arrays;
import java.util.Map;

public class ChangeSignTrigger extends Trigger {
    public ChangeSignTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }

    @EventHandler
    public void onSignChance(SignChangeEvent e){
        executeCommands(e, e.getPlayer(), null, Map.of("lines", Arrays.toString(e.getLines())));
    }

}
