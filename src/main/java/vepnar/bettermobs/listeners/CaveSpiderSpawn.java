package vepnar.bettermobs.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import vepnar.bettermobs.genericMobs.GenericMob;

public class CaveSpiderSpawn extends GenericMob {

    private double spawnProbability;
    private boolean onlyNatural;

    public CaveSpiderSpawn(JavaPlugin javaPlugin) {
        super(javaPlugin);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.SPIDER) return;
        if (spawnProbability < Math.random()) return;

        boolean naturalSpawnReason = event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL);
        if (onlyNatural && !naturalSpawnReason) return;

        Location location = event.getLocation();
        event.getEntity().remove();
        // TODO: Research when spawnEntity produces an NullPointerException
        location.getWorld().spawnEntity(location, EntityType.CAVE_SPIDER);
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
