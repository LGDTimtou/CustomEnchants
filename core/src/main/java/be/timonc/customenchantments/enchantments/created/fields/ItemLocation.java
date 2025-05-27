package be.timonc.customenchantments.enchantments.created.fields;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum ItemLocation {

    MAIN_HAND(player -> new ArrayList<>(List.of(player.getInventory().getItemInMainHand()))),
    OFF_HAND(player -> new ArrayList<>(List.of(player.getInventory().getItemInOffHand()))),
    ARMOR_SLOTS(player -> Arrays.asList(player.getInventory().getArmorContents())),
    HOTBAR(player -> IntStream.range(0, 9)
                              .mapToObj(i -> player.getInventory().getItem(i))
                              .collect(Collectors.toList())),
    INVENTORY(player -> IntStream.range(9, 36)
                                 .mapToObj(i -> player.getInventory().getItem(i))
                                 .collect(Collectors.toList())),
    ENDER_CHEST(player -> Arrays.asList(player.getEnderChest().getContents()));


    private final Function<Player, List<ItemStack>> getItems;

    ItemLocation(Function<Player, List<ItemStack>> getItems) {
        this.getItems = getItems;
    }

    public List<ItemStack> getCustomEnchantedItems(Player player) {
        return this.getItems.apply(player);
    }
}
