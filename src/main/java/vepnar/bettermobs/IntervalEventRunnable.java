package vepnar.bettermobs;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class IntervalEventRunnable extends BukkitRunnable {

    private static IntervalEventRunnable instance;
    private long interval;
    private int taskId;
    private Main core;

    public static IntervalEventRunnable getInstance() {
        if (instance == null) {
            instance = new IntervalEventRunnable();
        }
        return instance;
    }

    public void stop() {
        // Don't allow stopping when it has never been created before.
        if (instance == null || core == null) return;

        BukkitScheduler scheduler = core.getServer().getScheduler();
        if (taskId == 0 || !scheduler.isCurrentlyRunning(taskId)) return;
        scheduler.cancelTask(taskId);

        // Destroy old instance and replace it with a new one.
        instance = null;
    }

    public void start(Main main, long interval) {
        if (taskId != 0) {
            core.debug("Tried to create IntervalEvent while it is already running.");
            return;
        }

        this.interval = interval;
        this.core = main;

        taskId = this.runTaskTimer(core, 0, interval).getTaskId();
    }

    @Override
    public void run() {
        IntervalEvent event = new IntervalEvent(interval);
        core.getServer().getPluginManager().callEvent(event);
        core.debug("Interval");
    }
}
