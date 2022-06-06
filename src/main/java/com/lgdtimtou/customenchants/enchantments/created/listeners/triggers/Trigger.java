package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.Main;
import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchants.other.FileFunction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.stream.Collectors;

class Trigger implements CustomEnchantListener {

    private static final Random RG = new Random();

    private static final Map<Player, Set<Enchantment>> pendingCooldown = new HashMap<>();


    private final Enchantment enchantment;

    private final int[] cooldownArr;
    private final double[] chanceArr;
    private final boolean[] cancelledArr;
    private final List<List<String>> commandsList;

    private int level;
    private List<String> commands;


    public Trigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels){
        this.enchantment = enchantment;
        int maxLvl = levels.size();
        cooldownArr = new int[maxLvl];
        chanceArr = new double[maxLvl];
        cancelledArr = new boolean[maxLvl];
        commandsList = new ArrayList<>();
        for (int i = 0; i < maxLvl; i++) {
            CustomEnchantBuilder.CustomEnchantLevelInfo level = levels.get(i);
            cooldownArr[i] = level.getCooldown();
            chanceArr[i] = level.getChance();
            cancelledArr[i] = level.isEventCancelled();
            commandsList.add(level.getCommands());
        }
    }

    protected boolean defaultChecks(Player player){
        if (player == null)
            return false;
        PlayerInventory inv = player.getInventory();
        Location location = player.getLocation();
        if (inv.getItemInMainHand().getItemMeta() == null)
            return false;
        if (!inv.getItemInMainHand().containsEnchantment(this.enchantment))
            return false;

        pendingCooldown.computeIfAbsent(player, v -> new HashSet<>());
        if (pendingCooldown.get(player).contains(this.enchantment))
            return false;



        level = inv.getItemInMainHand().getEnchantmentLevel(this.enchantment);
        if (RG.nextInt(10001) > getChance())
            return false;


        //Replace global parameters
        //player's name
        commands = commandsList.get(level - 1).stream().map(command -> command.replace("%player%", player.getDisplayName())).collect(Collectors.toList());
        //Coordinates
        commands = commands.stream().map(c -> c.replace("%x%", String.valueOf(location.getX()))
                .replace("%y%", String.valueOf(location.getY())).replace("%z%", String.valueOf(location.getZ()))).collect(Collectors.toList());

        try {
            commands = FileFunction.parse(commands);
        } catch (NumberFormatException e){
            return false;
        }

        return true;
    }

    protected void dispatchCommands(Player player, Map<String, String> parameters){
        replaceParameters(parameters);
        commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        if (getCooldown() > 0){
            pendingCooldown.get(player).add(this.enchantment);
            Bukkit.getScheduler().runTaskLater(Main.getMain(), v -> pendingCooldown.get(player).remove(this.enchantment), getCooldown() * 20L);
        }
    }

    private void replaceParameters(Map<String, String> map) {
        map.forEach((key, value) -> commands = commands.stream().map(command -> command.replace("%" + key + "%", value)).collect(Collectors.toList()));
    }

    protected boolean isCancelled() {
        return cancelledArr[level - 1];
    }

    private int getCooldown(){
        return cooldownArr[level - 1];
    }

    private int getChance(){
        return (int) chanceArr[level - 1] * 100;
    }




}
