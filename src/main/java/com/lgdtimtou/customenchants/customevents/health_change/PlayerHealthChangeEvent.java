package com.lgdtimtou.customenchants.customevents.health_change;

import com.lgdtimtou.customenchants.customevents.CustomEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlayerHealthChangeEvent extends CustomEvent implements Cancellable {
    private final double healthChange;
    private final double health;
    private final double previous_health;
    private final Player player;
    private boolean isCancelled;
    PlayerHealthChangeEvent(double healthChange, Player player){
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

    public Player getPlayer(){
        return player;
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
