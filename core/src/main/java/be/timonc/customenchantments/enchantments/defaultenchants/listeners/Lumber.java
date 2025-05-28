package be.timonc.customenchantments.enchantments.defaultenchants.listeners;

import be.timonc.customenchantments.Main;
import be.timonc.customenchantments.enchantments.defaultenchants.DefaultCustomEnchant;
import be.timonc.customenchantments.enchantments.defaultenchants.DefaultTriggerListener;
import be.timonc.customenchantments.other.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Lumber extends DefaultTriggerListener {


    private static final int MAX_BROKEN_LOGS = 20;
    private static final Map<Player, Integer> activeTasks = new HashMap<>();
    private static final Map<Player, Set<Location>> breakingTree = new HashMap<>();


    @EventHandler
    public void logBreakEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();
        DefaultCustomEnchant defaultCustomEnchant = DefaultCustomEnchant.LUMBER;

        if (!defaultCustomEnchant.check(player)) return;
        if (breakingTree.get(player) != null) return;
        if (!isLog(e.getBlock().getType())) return;

        ItemStack enchantedItem = Util.getEnchantedItem(player, defaultCustomEnchant.get());
        if (enchantedItem == null)
            return;

        breakingTree.put(player, new HashSet<>());
        activeTasks.put(player, 0);
        breakTreeUpwards(e.getBlock().getLocation(), player, 0);
    }

    private void breakTreeUpwards(Location location, Player player, int counter) {
        if (counter >= MAX_BROKEN_LOGS) return;

        breakingTree.get(player).add(location);
        activeTasks.put(player, activeTasks.get(player) + 1);
        Bukkit.getScheduler().runTaskLater(
                Main.getMain(),
                () -> runPlayerBreakLog(player, location),
                counter * 2L
        );
        for (int y = 0; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Location newLocation = location.clone().add(x, y, z);
                    if (isLog(newLocation.getBlock().getType()) && !breakingTree.get(player).contains(newLocation))
                        breakTreeUpwards(newLocation, player, counter + 1);
                }
            }
        }
    }

    private void runPlayerBreakLog(Player player, Location location) {
        player.breakBlock(location.getBlock());
        activeTasks.put(player, activeTasks.get(player) - 1);

        if (activeTasks.get(player) == 0)
            breakingTree.remove(player);
    }

    private boolean isLog(Material material) {
        return material.name().endsWith("_LOG");
    }
}
