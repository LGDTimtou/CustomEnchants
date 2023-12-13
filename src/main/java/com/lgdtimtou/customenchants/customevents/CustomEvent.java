package com.lgdtimtou.customenchants.customevents;

import com.lgdtimtou.customenchants.customevents.armor_equip.ArmorListener;
import com.lgdtimtou.customenchants.customevents.health_change.PlayerHealthChangeListeners;
import com.lgdtimtou.customenchants.other.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.Collections;

public class CustomEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    public CustomEvent(Player who) {
        super(who);
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static void register(){
        Util.registerListener(new PlayerHealthChangeListeners());
        Util.registerListener(new ArmorListener(Collections.emptyList()));
    }

}
