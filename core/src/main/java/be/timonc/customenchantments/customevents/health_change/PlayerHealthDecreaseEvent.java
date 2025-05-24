package be.timonc.customenchantments.customevents.health_change;

import be.timonc.customenchantments.customevents.CustomEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlayerHealthDecreaseEvent extends CustomEvent implements Cancellable {
    private final double decrease;
    private final double health;
    private final double previous_health;
    private boolean isCancelled;


    public PlayerHealthDecreaseEvent(final Player player, double decrease) {
        super(player);
        this.decrease = decrease;
        this.health = player.getHealth() - decrease;
        this.previous_health = player.getHealth();
        this.player = player;
    }


    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public double getDecrease() {
        return decrease;
    }

    public double getHealth() {
        return health;
    }

    public double getPrevious_health() {
        return previous_health;
    }
}
