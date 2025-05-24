package be.timonc.customenchantments.enchantments.created.fields.triggers;


import be.timonc.customenchantments.other.Util;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum TriggerConditionType {

    // Specific
    PLAYER(Player.class, (player, condStr) -> checkPattern(player.getDisplayName(), condStr), Map.of(
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
    BLOCK(Block.class, (block, condStr) -> checkPattern(block.getType().toString(), condStr), Map.of(
            "block",
            (block) -> block.getType().toString(),
            "block_x",
            (block) -> String.valueOf(block.getLocation().getX()),
            "block_y",
            (block) -> String.valueOf(block.getLocation().getY()),
            "block_z",
            (block) -> String.valueOf(block.getLocation().getZ())
    )),
    ENTITY(Entity.class, (entity, condStr) -> checkPattern(entity.getType().toString(), condStr), Map.of(
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
    ITEM(ItemStack.class, (item, condStr) -> checkPattern(item.getType().toString(), condStr), Map.of(
            "type",
            (item) -> item.getType().toString(),
            "name",
            (item) -> item.getItemMeta() == null ? "" : item.getItemMeta().getDisplayName(),
            "amount",
            (item) -> String.valueOf(item.getAmount())
    )),
    CAUSE(Enum.class, (cause, condStr) -> checkPattern(cause.name(), condStr), Map.of("cause", Enum::name)),
    INVENTORY(
            Inventory.class,
            (inv, condStr) -> checkPattern(inv.getType().name(), condStr),
            Map.of("inv_type", (inv) -> inv.getType().name())
    ),
    STRING(String.class, TriggerConditionType::checkPattern, Map.of("value", (str) -> str)),
    DOUBLE_EQUALS(
            Double.class,
            (dble, condStr) -> compareDoubleResult(dble, condStr) == 0,
            Map.of("value", String::valueOf)
    ),
    DOUBLE_GREATER_THAN(
            Double.class,
            (dble, condStr) -> compareDoubleResult(dble, condStr) > 0,
            Map.of("value", String::valueOf)
    ),
    DOUBLE_LESS_THAN(
            Double.class,
            (dble, condStr) -> compareDoubleResult(dble, condStr) < 0,
            Map.of("value", String::valueOf)
    ),
    NULL(),

    // Global
    DIMENSION(
            World.Environment.class,
            (env, condStr) -> checkPattern(env.name(), condStr),
            Map.of("name", World.Environment::name),
            player -> player.getWorld().getEnvironment()
    ),
    BIOME(
            Biome.class,
            (biome, condStr) -> checkPattern(biome.toString(), condStr),
            Map.of("biome", Object::toString),
            player -> player.getLocation().getBlock().getBiome()
    ),
    TIME(
            Long.class,
            (ticks, condStr) -> Util.TimeOfDay.fromTicks(ticks)
                                              .stream()
                                              .anyMatch((val) -> checkPattern(val.name(), condStr)),
            Map.of("ticks", String::valueOf,
                    "day_night", (ticks) -> Util.TimeOfDay.dayOrNight(ticks).name()
            ),
            player -> player.getWorld().getTime()
    ),
    BLOCK_FEET(
            BLOCK,
            "feet",
            player -> player.getLocation().getBlock()
    ),
    BLOCK_UNDER(
            BLOCK,
            "under",
            player -> player.getLocation().add(0, -1, 0).getBlock()
    ),
    BLOCK_HEAD(
            BLOCK,
            "head",
            player -> player.getLocation().add(0, 1, 0).getBlock()
    ),
    BLOCK_ABOVE(
            BLOCK,
            "above",
            player -> player.getLocation().add(0, 2, 0).getBlock()
    ),
    BOOTS(
            ITEM,
            "boots",
            player -> player.getInventory().getBoots() != null ?
                    player.getInventory().getBoots() : new ItemStack(Material.AIR)
    ),
    LEGGINGS(
            ITEM,
            "leggings",
            player -> player.getInventory().getLeggings() != null ?
                    player.getInventory().getLeggings() : new ItemStack(Material.AIR)
    ),
    CHESTPLATE(
            ITEM,
            "chestplate",
            player -> player.getInventory().getChestplate() != null ?
                    player.getInventory().getChestplate() : new ItemStack(Material.AIR)
    ),
    HELMET(
            ITEM,
            "helmet",
            player -> player.getInventory().getHelmet() != null ?
                    player.getInventory().getHelmet() : new ItemStack(Material.AIR)
    ),
    HAND(
            ITEM,
            "hand",
            player -> player.getInventory().getItemInMainHand()
    ),
    OFF_HAND(
            ITEM,
            "off_hand",
            player -> player.getInventory().getItemInOffHand()
    ),
    PLAYER_HEALTH_EQUALS(
            DOUBLE_EQUALS,
            "player_health",
            Damageable::getHealth
    ),
    PLAYER_HEALTH_LESS_THAN(
            DOUBLE_LESS_THAN,
            "player_health",
            Damageable::getHealth
    ),
    PLAYER_HEALTH_GREATER_THAN(
            DOUBLE_GREATER_THAN,
            "player_health",
            Damageable::getHealth
    );

    private Class<?> targetClass;
    private Function<Player, Object> getGlobalValue = null;
    private BiPredicate<Object, String> checker = (obj, val) -> true;
    private BiFunction<String, Object, Map<String, Supplier<String>>> conditionParameters = (prefix, obj) -> Map.of();

    <T> TriggerConditionType(TriggerConditionType childTriggerConditionType, String globalValuePrefix, Function<Player, T> getGlobalValue) {
        this.targetClass = childTriggerConditionType.targetClass;
        this.checker = childTriggerConditionType.checker;
        this.conditionParameters = (prefix, obj) -> childTriggerConditionType.conditionParameters.apply(
                globalValuePrefix,
                obj
        );
        this.getGlobalValue = getGlobalValue == null ? null : getGlobalValue::apply;
    }

    <T> TriggerConditionType(Class<T> targetClass, BiPredicate<T, String> checker, Map<String, Function<T, String>> parameters) {
        this(targetClass, checker, parameters, null);
    }

    <T> TriggerConditionType(Class<T> targetClass, BiPredicate<T, String> checker, Map<String, Function<T, String>> parameters, Function<Player, T> getGlobalValue) {
        this.targetClass = targetClass;
        this.checker = (obj, val) -> checker.test(targetClass.cast(obj), val);

        this.conditionParameters = (prefix, obj) -> parameters.entrySet()
                                                              .stream()
                                                              .collect(Collectors.toMap(
                                                                      entry -> prefix.isEmpty() ? entry.getKey()
                                                                              : entry.getKey().isEmpty() ? prefix
                                                                              : prefix + "_" + entry.getKey(),
                                                                      entry -> () -> entry.getValue()
                                                                                          .apply(targetClass.cast(obj))
                                                              ));

        this.getGlobalValue = getGlobalValue == null ? null : getGlobalValue::apply;
    }

    TriggerConditionType() {

    }


    private static boolean checkPattern(String objStr, String condStr) {
        return Pattern.compile(condStr.toLowerCase().replace("*", ".*")).matcher(objStr.toLowerCase()).matches();
    }

    private static int compareDoubleResult(Double dble, String condStr) {
        try {
            return dble.compareTo(Double.valueOf(condStr));
        } catch (NumberFormatException e) {
            return dble.compareTo(Double.NaN);
        }
    }

    public static Map<ConditionKey, Object> getGlobalConditionMap(Player player) {
        return Arrays.stream(values())
                     .filter(TriggerConditionType::isGlobal)
                     .collect(Collectors.toMap(
                             type -> new ConditionKey(type, ""),
                             type -> type.getGlobalValue.apply(player)
                     ));
    }

    public boolean checkCondition(Object object, String value) {
        return checker.test(object, value);
    }

    public Map<String, Supplier<String>> getConditionParameters(String prefix, Object object) {
        return conditionParameters.apply(prefix, object);
    }

    private boolean isGlobal() {
        return this.getGlobalValue != null;
    }
}
