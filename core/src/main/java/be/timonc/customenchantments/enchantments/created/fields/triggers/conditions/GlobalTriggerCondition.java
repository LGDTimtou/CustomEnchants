package be.timonc.customenchantments.enchantments.created.fields.triggers.conditions;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public enum GlobalTriggerCondition {

    // Global
    DIMENSION(
            TriggerConditionType.DIMENSION,
            player -> player.getWorld().getEnvironment()
    ),
    BIOME(
            TriggerConditionType.BIOME,
            player -> player.getLocation().getBlock().getBiome()
    ),
    TIME(
            TriggerConditionType.NUMBER,
            player -> player.getWorld().getTime()
    ),
    BLOCK_FEET(
            TriggerConditionType.BLOCK,
            player -> player.getLocation().getBlock()
    ),
    BLOCK_UNDER(
            TriggerConditionType.BLOCK,
            player -> player.getLocation().add(0, -1, 0).getBlock()
    ),
    BLOCK_HEAD(
            TriggerConditionType.BLOCK,
            player -> player.getLocation().add(0, 1, 0).getBlock()
    ),
    BLOCK_ABOVE(
            TriggerConditionType.BLOCK,
            player -> player.getLocation().add(0, 2, 0).getBlock()
    ),
    BOOTS(
            TriggerConditionType.ITEM,
            player -> player.getInventory().getBoots() != null ?
                    player.getInventory().getBoots() : new ItemStack(Material.AIR)
    ),
    LEGGINGS(
            TriggerConditionType.ITEM,
            player -> player.getInventory().getLeggings() != null ?
                    player.getInventory().getLeggings() : new ItemStack(Material.AIR)
    ),
    CHESTPLATE(
            TriggerConditionType.ITEM,
            player -> player.getInventory().getChestplate() != null ?
                    player.getInventory().getChestplate() : new ItemStack(Material.AIR)
    ),
    HELMET(
            TriggerConditionType.ITEM,
            player -> player.getInventory().getHelmet() != null ?
                    player.getInventory().getHelmet() : new ItemStack(Material.AIR)
    ),
    HAND(
            TriggerConditionType.ITEM,
            player -> player.getInventory().getItemInMainHand()
    ),
    OFF_HAND(
            TriggerConditionType.ITEM,
            player -> player.getInventory().getItemInOffHand()
    ),
    PLAYER_HEALTH(
            TriggerConditionType.NUMBER,
            Damageable::getHealth
    );

    private final TriggerConditionType type;
    private final Function<Player, Object> getValue;

    <T> GlobalTriggerCondition(TriggerConditionType type, Function<Player, T> getValue) {
        this.type = type;
        this.getValue = getValue::apply;
    }

    public TriggerConditionType getType() {
        return type;
    }

    public Object getValue(Player player) {
        return getValue.apply(player);
    }
}
