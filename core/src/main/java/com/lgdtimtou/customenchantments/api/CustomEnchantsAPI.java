package com.lgdtimtou.customenchantments.api;

import com.lgdtimtou.customenchantments.enchantments.created.fields.CustomEnchantInstruction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

/**
 * The main API class for CustomEnchants.
 * <p>
 * This class provides static methods that external plugins and developers can use
 * to interact with CustomEnchants functionality.
 * </p>
 * <p>
 * All API methods are designed to be stable and safe for external usage.
 * </p>
 */
public class CustomEnchantsAPI {

    /**
     * Parses the input string by replacing parameters and evaluating all embedded expressions wrapped in <code>$[ ... ]</code>.
     * <p>
     * Parameters within the expressions are replaced using the provided map of parameter suppliers.
     * If PlaceholderAPI is installed, placeholders are also processed and replaced.
     * The expressions themselves are then evaluated using the EvalEx library.
     * </p>
     *
     * @param input      The input string containing one or more parameters and expressions wrapped as <code>$[expression]</code>.
     * @param player     The player context used for placeholder replacements (e.g., PlaceholderAPI).
     * @param parameters A map of parameter names to suppliers providing dynamic values to be used in expression evaluation.
     * @return The input string with all parameters replaced and all <code>$[ ... ]</code> expressions evaluated and replaced with their results.
     */
    @NotNull
    public static String parse(@NotNull String input, @NotNull Player player, @NotNull Map<String, Supplier<String>> parameters) {
        return CustomEnchantInstruction.parseNestedExpression(input, player, parameters);
    }
}
