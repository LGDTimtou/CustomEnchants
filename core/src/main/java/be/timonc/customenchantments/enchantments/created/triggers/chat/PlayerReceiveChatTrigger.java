package be.timonc.customenchantments.enchantments.created.triggers.chat;

import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroup;
import be.timonc.customenchantments.enchantments.created.fields.triggers.conditions.TriggerConditionGroupType;
import be.timonc.customenchantments.enchantments.created.triggers.TriggerListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;
import java.util.Set;

public class PlayerReceiveChatTrigger extends TriggerListener {

    private final TriggerConditionGroup messageConditions = new TriggerConditionGroup(
            "message", TriggerConditionGroupType.STRING
    );
    private final TriggerConditionGroup lengthConditions = new TriggerConditionGroup(
            "length", TriggerConditionGroupType.NUMBER
    );

    public PlayerReceiveChatTrigger(TriggerInvoker triggerInvoker) {
        super(triggerInvoker);
    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        for (Player player : Bukkit.getOnlinePlayers())
            if (player != e.getPlayer())
                triggerInvoker.trigger(e, player, Map.of(
                        messageConditions,
                        e.getMessage(),
                        lengthConditions,
                        e.getMessage().length()
                ));
    }

    @Override
    protected Set<TriggerConditionGroup> getConditionGroups() {
        return Set.of(messageConditions, lengthConditions);
    }
}
