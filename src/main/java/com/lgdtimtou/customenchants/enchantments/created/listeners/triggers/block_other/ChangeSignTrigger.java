package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.block_other;

import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchants.enchantments.created.listeners.triggers.Trigger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Arrays;
import java.util.Map;

public class ChangeSignTrigger extends Trigger {
    public ChangeSignTrigger(Enchantment enchantment, EnchantTriggerType type) {
        super(enchantment, type);
    }

    @EventHandler
    public void onSignChance(SignChangeEvent e){
        executeCommands(e, e.getPlayer(), null, Map.of("lines", Arrays.toString(e.getLines())));
    }

}
