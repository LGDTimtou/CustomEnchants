package com.lgdtimtou.customenchants.customevents;

import com.lgdtimtou.customenchants.customevents.health_change.PlayerHealthChangeListeners;
import com.lgdtimtou.customenchants.other.Util;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static void register(){
        Util.registerListener(new PlayerHealthChangeListeners());
    }

}
