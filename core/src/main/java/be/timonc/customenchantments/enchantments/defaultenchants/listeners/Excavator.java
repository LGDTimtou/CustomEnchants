package be.timonc.customenchantments.enchantments.defaultenchants.listeners;

import be.timonc.customenchantments.enchantments.defaultenchants.DefaultCustomEnchant;
import be.timonc.customenchantments.enchantments.defaultenchants.DefaultTriggerListener;
import be.timonc.customenchantments.other.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class Excavator extends DefaultTriggerListener {

    private final Set<Player> antiRecursion = new HashSet<>();

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        Player player = e.getPlayer();
        DefaultCustomEnchant defaultCustomEnchant = DefaultCustomEnchant.EXCAVATOR;

        if (antiRecursion.contains(player)) return;
        if (!defaultCustomEnchant.check(player)) return;
        if (Util.getEnchantedItem(player, defaultCustomEnchant.get()) == null) return;

        antiRecursion.add(player);

        Block centerBlock = e.getBlock();
        Material centerMaterial = centerBlock.getType();
        Vector direction = player.getLocation().getDirection();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Block adjacentBlock;
                if (Math.abs(direction.getY()) > 0.5)
                    adjacentBlock = centerBlock.getRelative(i, 0, j);
                else if (Math.abs(direction.getX()) > Math.abs(direction.getZ()))
                    adjacentBlock = centerBlock.getRelative(0, i, j);
                else
                    adjacentBlock = centerBlock.getRelative(i, j, 0);

                if (adjacentBlock.getType() == centerMaterial)
                    player.breakBlock(adjacentBlock);
            }
        }

        antiRecursion.remove(player);
    }
}
