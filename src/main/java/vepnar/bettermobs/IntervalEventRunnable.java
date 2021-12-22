package vepnar.bettermobs;

import org.bukkit.scheduler.BukkitRunnable;

public class IntervalEventRunnable extends BukkitRunnable {

    private static IntervalEventRunnable instance;
    private long interval;
    private int taskId = 0;
    private Main core;

    public static IntervalEventRunnable getInstance() {
        if (instance == null) {
            instance = new IntervalEventRunnable();
        }
        return instance;
    }

    public void stop() {
        if (taskId != 0) {
            core.getServer().getScheduler().cancelTask(taskId);
        }
    }

    public void start(Main main, long interval) {
        this.interval = interval;
        this.core = main;

        // Cancel task when it is already running.
        if (taskId != 0) {
            core.getServer().getScheduler().cancelTask(taskId);
        }
        taskId = this.runTaskTimer(core, 0, interval).getTaskId();
    }

    @Override
    public void run() {
        IntervalEvent event = new IntervalEvent(interval);
        core.getServer().getPluginManager().callEvent(event);
    }
}
