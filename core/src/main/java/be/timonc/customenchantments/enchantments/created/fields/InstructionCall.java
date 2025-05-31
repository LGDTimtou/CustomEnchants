package be.timonc.customenchantments.enchantments.created.fields;

import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Supplier;

public class InstructionCall {

    private static final Map<Key, Set<InstructionCall>> instructionCalls = new HashMap<>();
    private final Player player;
    private final CustomEnchant customEnchant;
    private final Map<String, Supplier<String>> parameters;
    private boolean cancelled = false;

    public InstructionCall(Queue<Instruction> instructions, Player player, CustomEnchant customEnchant, TriggerType triggerType, Map<String, Supplier<String>> parameters, Runnable onComplete) {
        this.player = player;
        this.customEnchant = customEnchant;
        this.parameters = parameters;

        Set<InstructionCall> instructionCallSet = instructionCalls.computeIfAbsent(
                Key.of(player, customEnchant, triggerType),
                k -> new HashSet<>()
        );
        instructionCallSet.add(this);

        executeInstructionQueue(
                new ArrayDeque<>(instructions),
                () -> {
                    instructionCallSet.remove(this);
                    onComplete.run();
                }
        );
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
