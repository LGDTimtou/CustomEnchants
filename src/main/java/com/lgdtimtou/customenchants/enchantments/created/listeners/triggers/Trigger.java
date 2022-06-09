package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.Main;
import com.lgdtimtou.customenchants.enchantments.CustomEnchant;
import com.lgdtimtou.customenchants.enchantments.created.listeners.CustomEnchantListener;
import com.lgdtimtou.customenchants.other.FileFunction;
import com.lgdtimtou.customenchants.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.stream.Collectors;

class Trigger implements CustomEnchantListener {

    private static final Random RG = new Random();

    private static final Map<Player, Set<Enchantment>> pendingCooldown = new HashMap<>();


    private final CustomEnchant enchantment;


    private int level;
    private List<String> commands;


    public Trigger(Enchantment enchantment){
        this.enchantment = CustomEnchant.get(enchantment.getKey().getKey());
    }


    protected void executeCommands(Event e, Player player, ItemStack item, Map<String, String> parameters){
        if (player == null)
            return;
        PlayerInventory inv = player.getInventory();
        Location location = player.getLocation();

        ItemStack enchantedItem = Util.containsEnchant(inv, this.enchantment.getEnchantment());
        if (enchantedItem == null || (item != null && item != enchantedItem))
            return;

        //Check if this enchantment is still in cooldown for the player
        pendingCooldown.computeIfAbsent(player, v -> new HashSet<>());
        if (pendingCooldown.get(player).contains(this.enchantment.getEnchantment()))
            return;

        //Get the level of the enchantment
        level = Util.getLevel(enchantedItem, this.enchantment.getEnchantment());

        //Return if chance didn't trigger
        if (RG.nextInt(10001) > enchantment.getChance(level))
            return;

        //Cancel event if specified to do so
        if (e instanceof Cancellable event)
            event.setCancelled(enchantment.isCancelled(level));


        //Replace global parameters
        //player's name
        commands = enchantment.getCommands(level - 1).stream().map(command -> command.replace("%player%", player.getDisplayName())).collect(Collectors.toList());
        //Coordinates
        commands = commands.stream().map(c -> c.replace("%x%", String.valueOf(location.getX()))
                .replace("%y%", String.valueOf(location.getY())).replace("%z%", String.valueOf(location.getZ()))).collect(Collectors.toList());

        try {
            commands = FileFunction.parse(commands);
        } catch (NumberFormatException exception){
            return;
        }

        dispatchCommands(player, parameters);
    }

    private void dispatchCommands(Player player, Map<String, String> parameters){
        replaceParameters(parameters);
        commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        if (enchantment.getCooldown(level) > 0){
            pendingCooldown.get(player).add(this.enchantment.getEnchantment());
            Bukkit.getScheduler().runTaskLater(Main.getMain(), v -> pendingCooldown.get(player).remove(this.enchantment.getEnchantment()), enchantment.getCooldown(level) * 20L);
        }
    }

    private void replaceParameters(Map<String, String> map) {
        map.forEach((key, value) -> commands = commands.stream().map(command -> command.replace("%" + key + "%", value)).collect(Collectors.toList()));
    }




}
