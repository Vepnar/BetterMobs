package vepnar.bettermobs.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import vepnar.bettermobs.Main;

public class GenericRunnable extends BukkitRunnable {

    protected static GenericRunnable instance;
    protected static Main CORE;
    protected BukkitTask task;

    public void stop() {
        // Don't allow stopping when it has never been created before.
        if (instance == null || CORE == null) return;

        BukkitScheduler scheduler = CORE.getServer().getScheduler();
        if (task == null || task.isCancelled()) return;
        task.cancel();
        // Destroy old instance and replace it with a new one.
        instance = null;
        task = null;
    }

    public boolean start(Main main, String name) {
        if (task != null) {
            CORE.debug("Tried to create " + name + " while it is already running.");
            return false;
        }
        CORE = main;
        return true;

    }

    @Override
    public void run() {
    }
}