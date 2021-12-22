package vepnar.bettermobs.listeners;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;

public class CaveSpiderSpawn extends GenericMob {

    private double spawnProbability;
    private boolean onlyNatural;

    public CaveSpiderSpawn(Main javaPlugin) {
        super(javaPlugin);

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.SPIDER) return;
        if (spawnProbability < Math.random()) return;

        boolean naturalSpawnReason = event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL);
        if (onlyNatural && !naturalSpawnReason) return;

        Location location = event.getLocation();
        event.getEntity().remove();
        // TODO: Research when spawnEntity produces an NullPointerException
        location.getWorld().spawnEntity(location, EntityType.CAVE_SPIDER);
        event.setCancelled(true);
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        spawnProbability = this.config.getDouble("spawnPercentage", 0) / 100;
        onlyNatural = this.config.getBoolean("onlyNatural", true);
    }

    @Override
    public String getName() {
        return "CaveSpiderSpawn";
    }

    @Override
    public void enable() {
        super.enable();
        core.getServer().getPluginManager().registerEvents(this, core);
    }

    @Override
    public void disable() {
        super.disable();
        HandlerList.unregisterAll(this);
    }
}
