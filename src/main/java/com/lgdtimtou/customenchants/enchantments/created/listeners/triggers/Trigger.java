package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchants.other.FileFunction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

    private final double[] chanceArr;
    private final boolean[] cancelledArr;
    private final List<List<String>> commandsList;

    private boolean cancelled;
    private List<String> commands;


    public Trigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels){
        this.enchantment = enchantment;
        int maxLvl = levels.size();
        chanceArr = new double[maxLvl];
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
        Player player = players[0];
        if (player == null)
            return false;
        PlayerInventory inv = player.getInventory();
        Location location = player.getLocation();
        if (inv.getItemInMainHand().getItemMeta() == null)
            return false;
        if (!inv.getItemInMainHand().containsEnchantment(this.enchantment))
            return false;


        int level = inv.getItemInMainHand().getEnchantmentLevel(this.enchantment);
        double chance = chanceArr[level - 1];
        if (RG.nextInt(10001) > (chance * 100))
            return false;

        cancelled = cancelledArr[level - 1];

        //Replace Parameters
        //player's name
        commands = commandsList.get(level - 1).stream().map(command -> command.replace("%player%", players.length > 1 ? players[1].getDisplayName() : player.getDisplayName())).collect(Collectors.toList());
        //Coordinates
        commands = commands.stream().map(c -> c.replace("%x%", String.valueOf(location.getX()))
                .replace("%y%", String.valueOf(location.getY())).replace("%z%", String.valueOf(location.getZ()))).collect(Collectors.toList());
        //Killer's name
        if (players.length > 1)
            commands = commands.stream().map(command -> command.replace("%killer%", player.getDisplayName())).collect(Collectors.toList());

        try {
            commands = FileFunction.parse(commands);
        } catch (NumberFormatException e){
            return false;
        }

        return true;
    }

    protected void dispatchCommands(){
        getCommands().forEach(System.out::println);
        getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    protected boolean isCancelled() {
        return cancelled;
    }

    protected List<String> getCommands() {
        return commands;
    }
}
