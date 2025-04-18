package com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.click;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.ConditionKey;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.EnchantTriggerType;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.Trigger;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.triggers.TriggerConditionType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class LeftClickItemTrigger extends Trigger {
    public LeftClickItemTrigger(CustomEnchant customEnchant, EnchantTriggerType type) {
        super(customEnchant, type);
    }


    @EventHandler
    public void onLeftClickItem(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (!(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)) return;

        ItemStack main_item = e.getPlayer().getInventory().getItem(EquipmentSlot.HAND);
        ItemStack off_item = e.getPlayer().getInventory().getItem(EquipmentSlot.OFF_HAND);

        executeCommands(e, e.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.ITEM, "hand_item"),
                main_item == null ? new ItemStack(Material.AIR) : main_item,
                new ConditionKey(TriggerConditionType.ITEM, "off_hand_item"),
                off_item == null ? new ItemStack(Material.AIR) : off_item
        ), Map.of());
    }
}

