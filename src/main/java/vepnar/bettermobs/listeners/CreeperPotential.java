package vepnar.bettermobs.listeners;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;

public class CreeperPotential extends GenericMob {

    private double spawnProbability;
    private boolean onlyNatural;

    public CreeperPotential(Main javaPlugin) {
        super(javaPlugin, "CreeperPotential", 1);

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.CREEPER) return;
        if (spawnProbability < Math.random()) return;

        boolean naturalSpawnReason = event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL);
        if (onlyNatural && !naturalSpawnReason) return;

        Creeper target = (Creeper) event.getEntity();
        target.setPowered(true);

    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        spawnProbability = this.config.getDouble("spawnPercentage", 0) / 100;
        onlyNatural = this.config.getBoolean("onlyNatural", true);
    }
}
