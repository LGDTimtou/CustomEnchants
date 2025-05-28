package be.timonc.customenchantments.enchantments.created.fields.triggers.conditions;


import be.timonc.customenchantments.other.Util;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TriggerConditionGroupType {

    PLAYER(Player.class, Map.of(
            "",
            Player::getName,
            "uuid",
            Player::getUniqueId,
            "health",
            Player::getHealth,
            "x",
            player -> player.getLocation().getX(),
            "y",
            player -> player.getLocation().getY(),
            "z",
            player -> player.getLocation().getZ(),
            "pitch",
            player -> player.getLocation().getPitch(),
            "yaw",
            player -> player.getLocation().getYaw()
    )),
    BLOCK(Block.class, Map.of(
            "",
            Block::getType,
            "x",
            Block::getX,
            "y",
            Block::getY,
            "z",
            Block::getZ
    )),
    ENTITY(Entity.class, Map.of(
            "",
            Entity::getType,
            "x",
            (entity) -> entity.getLocation().getX(),
            "y",
            (entity) -> entity.getLocation().getY(),
            "z",
            (entity) -> entity.getLocation().getZ(),
            "health",
            (entity) -> entity instanceof Damageable damageable ? damageable.getHealth() : 0
    )),
    ITEM(ItemStack.class, Map.of(
            "type",
            ItemStack::getType,
            "name",
            (item) -> item.getItemMeta() == null ? "" : item.getItemMeta().getDisplayName(),
            "amount",
            ItemStack::getAmount
    )),
    CAUSE(Enum.class, Map.of(
            "cause",
            Enum::name
    )),
    INVENTORY(Inventory.class, Map.of(
            "type",
            (inv) -> inv.getType().name()
    )),
    STRING(String.class, Map.of(
            "",
            (str) -> str
    )),
    NUMBER(Double.class, Map.of(
            "",
            String::valueOf
    )),
    DIMENSION(
            World.Environment.class, Map.of(
            "",
            World.Environment::name
    )),
    BIOME(Biome.class, Map.of(
            "",
            Object::toString
    ));


    private final Function<String, Set<TriggerCondition>> conditionSupplier;
    private final Function<String, Function<Object, Map<TriggerCondition, Object>>> conditionsMapSupplier;


    <T> TriggerConditionGroupType(Class<T> targetClass, Map<String, Function<T, Object>> conditionsMap) {
        this.conditionSupplier = (prefix) -> conditionsMap.keySet().stream().map(key -> new TriggerCondition(
                this,
                Util.getCombinedString(
                        "_",
                        prefix,
                        name().toLowerCase(),
                        key
                )
        )).collect(Collectors.toSet());
        this.conditionsMapSupplier = prefix ->
                obj -> conditionsMap.entrySet().stream().collect(Collectors.toMap(
                        entry -> new TriggerCondition(
                                this,
                                Util.getCombinedString(
                                        "_",
                                        prefix,
                                        name().toLowerCase(),
                                        entry.getKey()
                                )
                        ),
                        entry -> entry.getValue().apply(targetClass.cast(obj))
                ));
    }


    public Function<Object, Map<TriggerCondition, Object>> getConditionMapSupplier(String prefix) {
        return conditionsMapSupplier.apply(prefix);
    }

    public Set<TriggerCondition> getConditionSupplier(String prefix) {
        return conditionSupplier.apply(prefix);
    }
}
