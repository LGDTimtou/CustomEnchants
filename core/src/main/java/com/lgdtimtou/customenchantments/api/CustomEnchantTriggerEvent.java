package com.lgdtimtou.customenchantments.api;

import com.lgdtimtou.customenchantments.customevents.CustomEvent;
import com.lgdtimtou.customenchantments.enchantments.created.fields.triggers.TriggerType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

/**
 * This event is fired whenever the instructions of a specific trigger for a specific custom enchantment are executed.
 * <p>
 * It provides information about the original Bukkit event that triggered it, the player involved,
 * the type of trigger, the custom enchantment activated, and the dynamic parameters relevant to the execution context.
 * </p>
 */
public class CustomEnchantTriggerEvent extends CustomEvent {

    private final Event event;
    private final TriggerType triggerType;
    private final Enchantment enchantment;
    private final Map<String, Supplier<String>> parameters;

    /**
     * Constructs a new {@code CustomEnchantTriggerEvent}.
     * <p>
     * <b>Warning:</b> This event should never be instantiated or called manually by plugin developers.
     * It is fired internally by the CustomEnchants plugin when a trigger's instructions are executed.
     * </p>
     *
     * @param event       The original Bukkit event that caused this trigger (may be null).
     * @param triggerType The type of trigger that fired.
     * @param enchantment The custom enchantment associated with this trigger.
     * @param parameters  A map of parameter names to suppliers that provide dynamic parameter values during execution.
     */
    public CustomEnchantTriggerEvent(@Nullable Event event, @NotNull Player player, @NotNull TriggerType triggerType, @NotNull Enchantment enchantment, @NotNull Map<String, Supplier<String>> parameters) {
        super(player);
        this.event = event;
        this.triggerType = triggerType;
        this.enchantment = enchantment;
        this.parameters = parameters;
    }

    /**
     * Gets the original Bukkit event that caused this custom enchant trigger.
     *
     * @return The underlying Bukkit event, or {@code null} if none is associated (e.g. PLAYER_IDLE trigger).
     */
    @Nullable
    public Event getEvent() {
        return event;
    }

    /**
     * Gets the type of trigger that fired this event.
     *
     * @return The trigger type.
     */
    @NotNull
    public TriggerType getTriggerType() {
        return triggerType;
    }

    /**
     * Gets the custom enchantment associated with this trigger event.
     *
     * @return The custom enchantment.
     */
    @NotNull
    public Enchantment getEnchantment() {
        return enchantment;
    }

    /**
     * Gets an unmodifiable view of the dynamic parameters associated with this trigger event.
     * <p>
     * Each parameter name maps to a supplier that provides its current value.
     * </p>
     *
     * @return The map of parameter names to their suppliers.
     */
    @NotNull
    public Map<String, Supplier<String>> getParameters() {
        return parameters;
    }
}
