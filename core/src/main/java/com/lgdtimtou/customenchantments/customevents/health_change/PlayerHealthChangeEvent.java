package com.lgdtimtou.customenchantments.customevents.health_change;

import com.lgdtimtou.customenchantments.customevents.CustomEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlayerHealthChangeEvent extends CustomEvent implements Cancellable {
    private final double healthChange;
    private final double health;
    private final double previous_health;
    private boolean isCancelled;
    PlayerHealthChangeEvent(final Player player, double healthChange){
        super(player);
        this.healthChange = healthChange;
        this.health = player.getHealth() - healthChange;
        this.previous_health = player.getHealth();
        this.player = player;
        this.isCancelled = false;
    }


    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }


    public double getHealthChange() {
        return healthChange;
    }

    public double getPrevious_health() {
        return previous_health;
    }

    public double getHealth() {
        return health;
    }
}
