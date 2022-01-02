package vepnar.bettermobs.runnables;

import vepnar.bettermobs.IntervalEvent;
import vepnar.bettermobs.Main;

public class IntervalEventRunnable extends GenericRunnable {

    private static final String NAME = "IntervalEventRunnable";
    private static IntervalEventRunnable instance;
    private long interval;

    public static IntervalEventRunnable getInstance() {
        if (instance == null) {
            instance = new IntervalEventRunnable();
        }
        return instance;
    }

    public boolean start(Main main, long interval) {
        if (!super.start(main, NAME)) return false;
        try {
            this.interval = interval;
            task = this.runTaskTimer(CORE, 0L, interval);
            CORE.debug(NAME + " started!");
            return true;
        } catch (IllegalStateException exception) {
            return false;
        }
    }

    @Override
    public void run() {
        IntervalEvent event = new IntervalEvent(interval);
        CORE.getServer().getPluginManager().callEvent(event);
        CORE.debug("IntervalEvent called.");
    }
}