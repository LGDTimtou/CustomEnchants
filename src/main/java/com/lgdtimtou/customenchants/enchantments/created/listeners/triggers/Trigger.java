package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

class Trigger implements CustomEnchantListener {

    private static final Random RG = new Random();

    private final Enchantment enchantment;

    private final int[] chanceArr;
    private final boolean[] cancelledArr;
    private final List<List<String>> commandsList;

    private boolean cancelled;
    private List<String> commands;


    public Trigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels){
        this.enchantment = enchantment;
        int maxLvl = levels.size();
        chanceArr = new int[maxLvl];
        cancelledArr = new boolean[maxLvl];
        commandsList = new ArrayList<>();
        for (int i = 0; i < maxLvl; i++) {
            CustomEnchantBuilder.CustomEnchantLevelInfo level = levels.get(i);
            chanceArr[i] = level.getChance();
            cancelledArr[i] = level.isEventCancelled();
            commandsList.add(level.getCommands());
        }
    }

    protected boolean defaultChecks(Player... players){
        Player player = players.length > 1 ? players[1] : players[0];
        PlayerInventory inv = player.getInventory();
        if (inv.getItemInMainHand().getItemMeta() == null)
            return false;
        if (!inv.getItemInMainHand().containsEnchantment(this.enchantment))
            return false;
        if (players.length > 1 && players[1] == null)
            return false;

        int level = inv.getItemInMainHand().getEnchantmentLevel(this.enchantment);
        int chance = chanceArr[level - 1];
        int getal = RG.nextInt(101);
        System.out.println(getal +", " + chance);
        if (getal > chance)
            return false;

        cancelled = cancelledArr[level - 1];
        commands = commandsList.get(level - 1).stream().map(command -> command.replace("%player%", players.length > 1 ? players[0].getDisplayName() : player.getDisplayName())).collect(Collectors.toList());
        if (players.length > 1)
            commands = commands.stream().map(command -> command.replace("%killer%", player.getDisplayName())).collect(Collectors.toList());
        return true;
    }


    protected boolean isCancelled() {
        return cancelled;
    }

    protected List<String> getCommands() {
        return commands;
    }
}
