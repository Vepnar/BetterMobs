package vepnar.bettermobs.listeners;

import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;

public class NoDragonPerching extends GenericMob {
    public NoDragonPerching(Main javaPlugin) {
        super(javaPlugin, "NoDragonPerching", 1);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void noPerching(EnderDragonChangePhaseEvent event) {
        if (event.getNewPhase() == EnderDragon.Phase.FLY_TO_PORTAL)
            event.setCancelled(true);
    }
}
