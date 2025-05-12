package com.lgdtimtou.customenchantments.enchantments.created.fields.instructions.data;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchant;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum SaveContext {
    GLOBAL(player -> ""),
    WORLD(Entity::getWorld),
    DIMENSION(player -> player.getWorld().getEnvironment()),
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
