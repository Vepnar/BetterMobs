package vepnar.bettermobs.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import vepnar.bettermobs.Main;

public class GenericRunnable extends BukkitRunnable {

    protected static Main CORE;
    protected BukkitTask task;

    public void stop() {
        // Don't allow stopping when it has never been created before.
        if (CORE == null || task == null) return;

        if (task.isCancelled()) return;
        task.cancel();
    }

    public boolean start(Main main, String name) {
        BukkitScheduler scheduler = main.getServer().getScheduler();
        if (task != null && scheduler.isCurrentlyRunning(task.getTaskId())) {
            return false;
        }
        CORE = main;
        return true;

    }

    @Override
    public void run() {
    }
}