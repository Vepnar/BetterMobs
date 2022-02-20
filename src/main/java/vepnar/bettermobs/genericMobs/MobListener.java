package vepnar.bettermobs.genericMobs;

import org.bukkit.event.Listener;

public interface MobListener extends Listener {
    void reloadConfig();

    void initialize();

    void enable();

    void disable();

    boolean isEnabled();

    boolean isCompatible();

    String getName();
}