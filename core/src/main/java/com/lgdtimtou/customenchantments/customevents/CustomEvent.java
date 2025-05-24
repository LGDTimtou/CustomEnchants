package com.lgdtimtou.customenchantments.customevents;

import com.lgdtimtou.customenchantments.customevents.armor_equip.ArmorListener;
import com.lgdtimtou.customenchantments.customevents.health_change.PlayerHealthChangeListeners;
import com.lgdtimtou.customenchantments.other.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.Collections;

public class CustomEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    public CustomEvent(Player who) {
        super(who);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static void register() {
        Util.registerListener(new PlayerHealthChangeListeners());
        Util.registerListener(new ArmorListener(Collections.emptyList()));
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
