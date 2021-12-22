package vepnar.bettermobs;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IntervalEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    public final float interval;

    public IntervalEvent(float interval) {
        this.interval = interval;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
