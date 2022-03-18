package vepnar.bettermobs.listeners;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;

@SuppressWarnings("unused")
public class LunaticCreeper extends GenericMob {

    private boolean listenPower;

    private int explosionPower;
    private int explosionPowerCharged;
    private double spawnProbabilityCharged;
    private boolean onlyNaturalCharged;

    private int fuseTicks;
    private int fuseTicksCharged;
    private double spawnProbability;
    private boolean onlyNatural;


    public LunaticCreeper(Main javaPlugin) {
        super(javaPlugin, "LunaticCreeper", 1, 13);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.CREEPER) return;
        if (event.isCancelled()) return;

        Creeper target = (Creeper) event.getEntity();
        boolean naturalSpawnReason = event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL);

        if (target.isPowered()) {
            if (spawnProbabilityCharged < Math.random()) return;
            if (onlyNaturalCharged && !naturalSpawnReason) return;
            target.setMaxFuseTicks(fuseTicksCharged);
            target.setExplosionRadius(explosionPowerCharged);
        } else {
            if (spawnProbability < Math.random()) return;
            if (onlyNatural && !naturalSpawnReason) return;
            target.setMaxFuseTicks(fuseTicks);
            target.setExplosionRadius(explosionPower);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCreeperCharge(CreeperPowerEvent event) {
        if(listenPower) return;
        Creeper target = event.getEntity();

        if (target.isPowered()) {
            if (spawnProbabilityCharged < Math.random()) return;
            target.setMaxFuseTicks(fuseTicksCharged);
            target.setExplosionRadius(explosionPowerCharged);
        } else {
            if (spawnProbability < Math.random()) return;
            target.setMaxFuseTicks(fuseTicks);
            target.setExplosionRadius(explosionPower);
        }
    }

    @Override
    public void readConfig() {
        fuseTicksCharged = config.getInt("chargedCreeper.fuse", 3);
        explosionPowerCharged = config.getInt("chargedCreeper.explosionPower", 30);
        spawnProbabilityCharged = config.getDouble("chargedCreeper.spawnPercentage", 100) / 100;
        onlyNaturalCharged = config.getBoolean("chargedCreeper.onlyNatural", false);

        fuseTicks = config.getInt("normalCreeper.fuse", 3);
        explosionPower = config.getInt("normalCreeper.explosionPower", 6);
        spawnProbability = config.getDouble("normalCreeper.spawnPercentage", 100) / 100;
        onlyNatural = config.getBoolean("normalCreeper.onlyNatural", false);

        listenPower = config.getBoolean("listenToPowerEvent", true);
    }
}
