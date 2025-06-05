package be.timonc.customenchantments.enchantments.custom.fields.instructions.data;

import be.timonc.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum SaveContext {
    GLOBAL(player -> ""),
    DIMENSION(player -> player.getWorld().getEnvironment()),
    WORLD(Entity::getWorld),
    BIOME(player -> player.getWorld().getBiome(player.getLocation())),
    PLAYER(player -> player);

    private final Map<Object, Map<CustomEnchant, Map<String, String>>> savedValues = new HashMap<>();

    private final Function<Player, Object> getContextMapValue;


    SaveContext(Function<Player, Object> getContextMapValue) {
        this.getContextMapValue = getContextMapValue;
    }

    public String load(Player player, CustomEnchant customEnchant, String identifier, String defaultValue) {
        Object contextMapValue = getContextMapValue.apply(player);
        if (!savedValues.containsKey(contextMapValue)) return defaultValue;
        if (!savedValues.get(contextMapValue).containsKey(customEnchant)) return defaultValue;
        return savedValues.get(contextMapValue).get(customEnchant).getOrDefault(identifier, defaultValue);
    }

    public void save(Player player, CustomEnchant customEnchant, String identifier, String value) {
        Object contextMapValue = getContextMapValue.apply(player);
        savedValues.computeIfAbsent(contextMapValue, k -> new HashMap<>())
                   .computeIfAbsent(customEnchant, k -> new HashMap<>())
                   .put(identifier, value);
    }
}
