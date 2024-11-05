package com.lgdtimtou.customenchantments.enchantments.defaultenchants;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchantments.enchantments.defaultenchants.listeners.Excavator;
import com.lgdtimtou.customenchantments.enchantments.defaultenchants.listeners.Replenish;
import com.lgdtimtou.customenchantments.enchantments.defaultenchants.listeners.Telekinesis;

public enum DefaultCustomEnchant {

    //Enchantments
    REPLENISH("replenish", 1, new Replenish()),
    TELEKINESIS("telekinesis", 1, new Telekinesis()),
    EXCAVATOR("excavator", 1, new Excavator());

    private final String namespacedName;
    private final int maxLevel;
    private final CustomEnchantListener listener;

    private CustomEnchant enchantment;


    DefaultCustomEnchant(String namespacedName, int maxLevel, CustomEnchantListener listener){
        this.namespacedName = namespacedName;
        this.maxLevel = maxLevel;
        this.listener = listener;
    }

    public CustomEnchant get(){
        if (enchantment == null)
            this.enchantment = CustomEnchant.get(this.namespacedName);
        return enchantment;
    }

    public String getNamespacedName() {
        return namespacedName;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public CustomEnchantListener getListener() {
        return listener;
    }
}
