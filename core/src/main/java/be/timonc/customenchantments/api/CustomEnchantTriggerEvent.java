package be.timonc.customenchantments.api;

import be.timonc.customenchantments.customevents.CustomEvent;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
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
    private final ItemStack enchantedItem;
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
    public CustomEnchantTriggerEvent(@Nullable Event event, @NotNull Player player, @NotNull TriggerType triggerType, @NotNull Enchantment enchantment, @NotNull ItemStack enchantedItem, @NotNull Map<String, Supplier<String>> parameters) {
        super(player);
        this.event = event;
        this.triggerType = triggerType;
        this.enchantment = enchantment;
        this.enchantedItem = enchantedItem;
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
     * Gets the item that contains the enchantment that was triggered.
     *
     * @return The ItemStack
     */
    @NotNull
    public ItemStack getEnchantedItem() {
        return enchantedItem;
    }


    /**
     * Retrieves the value of a dynamic parameter as a String.
     *
     * @param key The name of the parameter to retrieve.
     * @return The parameter value as a String.
     * @throws NullPointerException if the parameter with the given key does not exist or its supplier returns null.
     */
    @NotNull
    public String getParameter(@NotNull String key) {
        return parameters.get(key).get();
    }

    /**
     * Retrieves the value of a dynamic parameter as an Integer.
     *
     * @param key The name of the parameter to retrieve.
     * @return The parameter value parsed as an Integer.
     * @throws NullPointerException  if the parameter with the given key does not exist or its supplier returns null.
     * @throws NumberFormatException if the parameter value cannot be parsed as an Integer.
     */
    @NotNull
    public Integer getIntParameter(@NotNull String key) {
        return Integer.valueOf(parameters.get(key).get());
    }

    /**
     * Retrieves the value of a dynamic parameter as a Double.
     *
     * @param key The name of the parameter to retrieve.
     * @return The parameter value parsed as a Double.
     * @throws NullPointerException  if the parameter with the given key does not exist or its supplier returns null.
     * @throws NumberFormatException if the parameter value cannot be parsed as a Double.
     */
    @NotNull
    public Double getDoubleParameter(@NotNull String key) {
        return Double.valueOf(parameters.get(key).get());
    }
}
