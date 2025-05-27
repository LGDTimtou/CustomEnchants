package be.timonc.customenchantments.enchantments.created.fields.triggers.conditions;


import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum TriggerConditionType {

    // Specific
    PLAYER(Player.class, Player::getName, Map.of(
            "",
            Player::getName,
            "x",
            (player) -> String.valueOf(player.getLocation().getX()),
            "y",
            (player) -> String.valueOf(player.getLocation().getY()),
            "z",
            (player) -> String.valueOf(player.getLocation().getZ()),
            "health",
            (player) -> String.valueOf(player.getHealth())
    )),
    BLOCK(Block.class, block -> block.getType().toString(), Map.of(
            "block",
            (block) -> block.getType().toString(),
            "block_x",
            (block) -> String.valueOf(block.getLocation().getX()),
            "block_y",
            (block) -> String.valueOf(block.getLocation().getY()),
            "block_z",
            (block) -> String.valueOf(block.getLocation().getZ())
    )),
    ENTITY(Entity.class, entity -> entity.getType().toString(), Map.of(
            "",
            (entity) -> entity.getType().toString(),
            "x",
            (entity) -> String.valueOf(entity.getLocation().getX()),
            "y",
            (entity) -> String.valueOf(entity.getLocation().getY()),
            "z",
            (entity) -> String.valueOf(entity.getLocation().getZ()),
            "health",
            (entity) -> String.valueOf(entity instanceof Damageable damageable ? damageable.getHealth() : 0)
    )),
    ITEM(ItemStack.class, item -> item.getType().toString(), Map.of(
            "type",
            (item) -> item.getType().toString(),
            "name",
            (item) -> item.getItemMeta() == null ? "" : item.getItemMeta().getDisplayName(),
            "amount",
            (item) -> String.valueOf(item.getAmount())
    )),
    CAUSE(Enum.class, Enum::name, Map.of("cause", Enum::name)),
    INVENTORY(
            Inventory.class,
            inv -> inv.getType().name(),
            Map.of("inv_type", (inv) -> inv.getType().name())
    ),
    STRING(String.class, str -> str, Map.of("value", (str) -> str)),
    NUMBER(
            Double.class,
            dble -> dble,
            Map.of("value", String::valueOf)
    ),
    DIMENSION(
            World.Environment.class,
            Enum::name,
            Map.of("name", World.Environment::name)
    ),
    BIOME(
            Biome.class,
            Enum::toString,
            Map.of("biome", Object::toString)
    );


    private final BiFunction<String, Object, Map<String, Supplier<String>>> conditionParameters;
    private final Function<Object, Comparable<?>> criteria;

    <T> TriggerConditionType(Class<T> targetClass, Function<T, Comparable<?>> criteria, Map<String, Function<T, String>> parameters) {
        this.criteria = obj -> criteria.apply(targetClass.cast(obj));
        this.conditionParameters = (prefix, obj) -> parameters.entrySet()
                                                              .stream()
                                                              .collect(Collectors.toMap(
                                                                      entry -> prefix.isEmpty() ? entry.getKey()
                                                                              : entry.getKey().isEmpty() ? prefix
                                                                              : prefix + "_" + entry.getKey(),
                                                                      entry -> () -> entry.getValue()
                                                                                          .apply(targetClass.cast(obj))
                                                              ));
    }


    public Map<String, Supplier<String>> getConditionParameters(String prefix, Object object) {
        return conditionParameters.apply(prefix, object);
    }

    public Comparable<?> getCriteria(Object object) {
        return criteria.apply(object);
    }
}
