package be.timonc.customenchantments.enchantments.created.fields.triggers.conditions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum GlobalTriggerCondition {

    // Global
    DIMENSION(
            TriggerConditionGroupType.DIMENSION,
            "",
            player -> player.getWorld().getEnvironment()
    ),
    BIOME(
            TriggerConditionGroupType.BIOME,
            "",
            player -> player.getLocation().getBlock().getBiome()
    ),
    TIME(
            TriggerConditionGroupType.NUMBER,
            "time",
            player -> (double) player.getWorld().getTime()
    ),
    BLOCK_FEET(
            TriggerConditionGroupType.BLOCK,
            "feet",
            player -> player.getLocation().getBlock()
    ),
    BLOCK_UNDER_FEET(
            TriggerConditionGroupType.BLOCK,
            "under_feet",
            player -> player.getLocation().add(0, -1, 0).getBlock()
    ),
    BLOCK_HEAD(
            TriggerConditionGroupType.BLOCK,
            "head",
            player -> player.getLocation().add(0, 1, 0).getBlock()
    ),
    BLOCK_ABOVE_HEAD(
            TriggerConditionGroupType.BLOCK,
            "above_head",
            player -> player.getLocation().add(0, 2, 0).getBlock()
    ),
    BOOTS(
            TriggerConditionGroupType.ITEM,
            "boots",
            player -> player.getInventory().getBoots() != null ?
                    player.getInventory().getBoots() : new ItemStack(Material.AIR)
    ),
    LEGGINGS(
            TriggerConditionGroupType.ITEM,
            "leggings",
            player -> player.getInventory().getLeggings() != null ?
                    player.getInventory().getLeggings() : new ItemStack(Material.AIR)
    ),
    CHESTPLATE(
            TriggerConditionGroupType.ITEM,
            "chestplate",
            player -> player.getInventory().getChestplate() != null ?
                    player.getInventory().getChestplate() : new ItemStack(Material.AIR)
    ),
    HELMET(
            TriggerConditionGroupType.ITEM,
            "helmet",
            player -> player.getInventory().getHelmet() != null ?
                    player.getInventory().getHelmet() : new ItemStack(Material.AIR)
    ),
    MAIN_HAND(
            TriggerConditionGroupType.ITEM,
            "main_hand",
            player -> player.getInventory().getItemInMainHand()
    ),
    OFF_HAND(
            TriggerConditionGroupType.ITEM,
            "off_hand",
            player -> player.getInventory().getItemInOffHand()
    ),
    PLAYER(
            TriggerConditionGroupType.PLAYER,
            "",
            player -> player
    );

    private static final Map<TriggerConditionGroup, Function<Player, Object>> globalConditionMap =
            Arrays.stream(values()).collect(Collectors.toMap(
                    TriggerConditionGroup::new,
                    globalTriggerCondition -> globalTriggerCondition.getValue
            ));


    private final TriggerConditionGroupType groupType;
    private final String prefix;
    private final Function<Player, Object> getValue;

    <T> GlobalTriggerCondition(TriggerConditionGroupType groupType, String prefix, Function<Player, T> getValue) {
        this.groupType = groupType;
        this.prefix = prefix;
        this.getValue = getValue::apply;
    }

    public static Set<TriggerConditionGroup> get() {
        return globalConditionMap.keySet();
    }

    public static Map<TriggerConditionGroup, Object> getMap(Player player) {
        return globalConditionMap.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().apply(player)
        ));
    }

    public TriggerConditionGroupType getGroupType() {
        return groupType;
    }

    public String getPrefix() {
        return prefix;
    }
}
