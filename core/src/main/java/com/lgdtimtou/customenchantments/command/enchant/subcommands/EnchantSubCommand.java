package com.lgdtimtou.customenchantments.command.enchant.subcommands;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import com.lgdtimtou.customenchantments.command.SubCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class EnchantSubCommand implements SubCommand {

    private final String label;

    public EnchantSubCommand(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    public abstract void execute(Player player, ItemStack item, CustomEnchant enchantment, int level);

}
