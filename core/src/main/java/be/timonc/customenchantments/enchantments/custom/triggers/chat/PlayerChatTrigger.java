package be.timonc.customenchantments.enchantments.custom.triggers.chat;

import be.timonc.customenchantments.enchantments.custom.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.custom.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.custom.triggers.TriggerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;
import java.util.Set;

public class PlayerChatTrigger extends TriggerListener {

    private final TriggerConditionGroup messageConditions = new TriggerConditionGroup(
            "message", TriggerConditionGroupType.STRING
    );
    private final TriggerConditionGroup lengthConditions = new TriggerConditionGroup(
            "length", TriggerConditionGroupType.NUMBER
    );

    public PlayerChatTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent e) {
        triggerInvoker.trigger(
                e, e.getPlayer(), Map.of(
                        messageConditions, e.getMessage(),
                        lengthConditions, e.getMessage().length()
                )
        );
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(messageConditions, lengthConditions);
    }
}
