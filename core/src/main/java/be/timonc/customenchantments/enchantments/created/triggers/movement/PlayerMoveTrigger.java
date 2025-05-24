package be.timonc.customenchantments.enchantments.created.triggers.movement;

import be.timonc.customenchantments.enchantments.created.fields.triggers.ConditionKey;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerConditionType;
import be.timonc.customenchantments.enchantments.created.fields.triggers.TriggerInvoker;
import be.timonc.customenchantments.enchantments.created.triggers.CustomEnchantListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;

public class PlayerMoveTrigger implements CustomEnchantListener {

    private final TriggerInvoker triggerInvoker;

    public PlayerMoveTrigger(TriggerInvoker type) {
        this.triggerInvoker = type;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        triggerInvoker.trigger(e, e.getPlayer(), Map.of(
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "from_x"), e.getFrom().getX(),
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "from_x"), e.getFrom().getX(),
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "from_x"), e.getFrom().getX(),
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "from_y"), e.getFrom().getY(),
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "from_y"), e.getFrom().getY(),
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "from_y"), e.getFrom().getY(),
                new ConditionKey(TriggerConditionType.DOUBLE_EQUALS, "from_z"), e.getFrom().getZ(),
                new ConditionKey(TriggerConditionType.DOUBLE_GREATER_THAN, "from_z"), e.getFrom().getZ(),
                new ConditionKey(TriggerConditionType.DOUBLE_LESS_THAN, "from_z"), e.getFrom().getZ()
        ), Map.of());
    }
}
