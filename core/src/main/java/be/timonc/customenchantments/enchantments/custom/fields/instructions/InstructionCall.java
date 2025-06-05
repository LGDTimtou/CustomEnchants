package be.timonc.customenchantments.enchantments.custom.fields.instructions;

import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.enchantments.custom.fields.Level;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerType;
import be.timonc.customenchantments.other.CustomEnchantLogFilter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Supplier;

public class InstructionCall {

    private static final Map<Key, Set<InstructionCall>> instructionCalls = new HashMap<>();
    private final Player player;
    private final CustomEnchant customEnchant;
    private final Map<String, Supplier<String>> parameters;
    private final List<String> cleanupCommands;
    private boolean cancelled = false;

    public InstructionCall(Player player, CustomEnchant customEnchant, Level level, TriggerType triggerType, Map<String, Supplier<String>> parameters, Runnable onComplete) {
        this.player = player;
        this.customEnchant = customEnchant;
        this.parameters = parameters;
        this.cleanupCommands = level.cleanupCommands();

        Set<InstructionCall> instructionCallSet = instructionCalls.computeIfAbsent(
                Key.of(player, customEnchant, triggerType),
                k -> new HashSet<>()
        );
        instructionCallSet.add(this);

        executeInstructionQueue(
                new ArrayDeque<>(level.instructions()),
                () -> {
                    instructionCallSet.remove(this);
                    onComplete.run();
                }
        );
    }

    public static void callCleanupCommands() {
        instructionCalls.values().forEach(set -> set.forEach(InstructionCall::cleanup));
    }

    private void cleanup() {
        cancel();
        CustomEnchantLogFilter.addFilter();
        cleanupCommands.forEach(cleanupCommand -> {
            String command = Instruction.parseNestedExpression(
                    cleanupCommand,
                    player,
                    parameters
            );
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
        CustomEnchantLogFilter.removeFilter();
    }

    public Player getPlayer() {
        return player;
    }

    public CustomEnchant getCustomEnchant() {
        return customEnchant;
    }

    public Map<String, Supplier<String>> getParameters() {
        return parameters;
    }

    private void cancel() {
        this.cancelled = true;
    }

    public void cancel(TriggerType triggerType) {
        Key key = Key.of(player, customEnchant, triggerType);
        instructionCalls.getOrDefault(key, Set.of()).forEach(InstructionCall::cancel);
    }

    private List<String> getCleanupCommands() {
        return cleanupCommands;
    }


    public void executeInstructionQueue(Queue<Instruction> instructionQueue, Runnable oncomplete) {
        Instruction instruction = instructionQueue.poll();
        if (instruction != null && !cancelled) {
            instruction.execute(
                    this,
                    () -> executeInstructionQueue(
                            instructionQueue,
                            oncomplete
                    )
            );
        } else oncomplete.run();
    }

    private record Key(Player player, CustomEnchant customEnchant, TriggerType triggerType) {
        private static Key of(Player player, CustomEnchant customEnchant, TriggerType triggerType) {
            return new Key(player, customEnchant, triggerType);
        }
    }
}
