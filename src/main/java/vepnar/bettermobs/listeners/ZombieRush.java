package vepnar.bettermobs.listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;

@SuppressWarnings("unused")
public class ZombieRush extends GenericMob {

    double minSpeed;
    double maxSpeed;
    double minBabySpeed;
    double maxBabySpeed;
    boolean onlyNatural;
    double rushProbability;

    public ZombieRush(Main javaPlugin) {
        super(javaPlugin, "ZombieRush", 1, 13);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSpawn(CreatureSpawnEvent event) {
        // Filter out all non-zombie creatures
        if(!(event.getEntity() instanceof Zombie))return;

        // Only natural zombies if enabled
        boolean naturalSpawnReason = event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL);
        if (onlyNatural && !naturalSpawnReason) return;

        // Verify if RNG thinks we should spawn quick zombies.
        if(shouldOccur(rushProbability)) return;

        Zombie zombie = (Zombie) event.getEntity();

        double multiplier = Math.random();
        double newSpeed;

        if (zombie.isAdult()) {
            newSpeed=minSpeed + (maxSpeed-minSpeed) * multiplier;
        } else newSpeed=minBabySpeed + (maxBabySpeed-minBabySpeed) * multiplier;
      zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(newSpeed);

    }

    @Override
    public void readConfig() {
        onlyNatural = config.getBoolean("onlyNatural", true);
        rushProbability = config.getDouble("rushPercentageChance", 0) / 100;

        minSpeed = config.getDouble("adult.minSpeed", 2.3) / 10;
        maxSpeed = config.getDouble("adult.maxSpeed", 3.4) / 10;
        minBabySpeed = config.getDouble("baby.maxSpeed", 3.4) / 10;
        maxBabySpeed = config.getDouble("baby.maxSpeed", 4.5) / 10;
    }
}
