package com.lgdtimtou.customenchants.customevents.health_decrease;

import com.lgdtimtou.customenchants.customevents.CustomEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlayerHealthDecreaseEvent extends CustomEvent implements Cancellable {
    private boolean isCancelled;
    private final double decrease;
    private final double health;
    private final double previous_health;
    private final Player player;


    public PlayerHealthDecreaseEvent(double decrease, Player player){
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

    public Player getPlayer() {
        return player;
    }

    public double getHealth() {
        return health;
    }

    public double getPrevious_health() {
        return previous_health;
    }
}
