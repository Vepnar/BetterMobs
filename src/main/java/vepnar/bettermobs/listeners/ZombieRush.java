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

    double min_speed;
    double max_speed;
    double min_baby_speed;
    double max_baby_speed;
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
            newSpeed=min_speed + (max_speed-min_speed) * multiplier;
        } else newSpeed=min_baby_speed + (max_baby_speed-min_baby_speed) * multiplier;
      zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(newSpeed);

    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        onlyNatural = config.getBoolean("onlyNatural", true);
        rushProbability = config.getDouble("rushPercentageChance", 0) / 100;

        min_speed = config.getDouble("adult.minSpeed", 2.3) / 10;
        max_speed = config.getDouble("adult.maxSpeed", 3.4) / 10;
        min_baby_speed = config.getDouble("baby.maxSpeed", 3.4) / 10;
        max_baby_speed = config.getDouble("baby.maxSpeed", 4.5) / 10;
    }


}
