package vepnar.bettermobs.updateChecker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import vepnar.bettermobs.Main;

public class UpdateListener implements Listener {

    public static UpdateListener instance;

    private UpdateListener() {

    }

    public static UpdateListener getInstance() {
        if (instance == null) {
            instance = new UpdateListener();
        }
        return instance;
    }

    public static void disable() {
        if (instance != null) {
            PlayerJoinEvent.getHandlerList().unregister(instance);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAdminJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Only notify players who have the given permission
        if (!player.hasPermission("bettermobs.messages.outdated")) return;

        // Send notification when outdated.
        if (UpdateCheckerRunnable.getState() == UpdateState.OUTDATED) {
            player.sendMessage(Main.FANCY_NAME + "is outdated. please install the newest version!");
        }

    }
}
