package vepnar.bettermobs.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTargetEvent;
import vepnar.bettermobs.IntervalEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;

import java.util.ArrayList;
import java.util.List;

public class ZombieMount extends GenericMob {

    int searchRadius;
    int mountRadius;
    double mountProbability;
    boolean brainWash;

    public ZombieMount(Main javaPlugin) {
        super(javaPlugin, "ZombieMount", 1);
    }

    public boolean isValidEntity(LivingEntity entity) {
        return !entity.isInsideVehicle() && entity.hasAI() && entity.isEmpty() && !entity.isLeashed() && entity.getFireTicks() == 0;
    }

    public List<LivingEntity> findValidRider(List<Entity> entities) {
        List<LivingEntity> foundEntities = new ArrayList<>();

        for (Entity entity : entities) {
            // Filter unneeded entities
            if (!(entity instanceof Zombie)) continue;
            Zombie lEntity = (Zombie) entity;

            // Check if the entity is restricted in some matter.
            if (!isValidEntity(lEntity) || lEntity.isAdult()) continue;

            foundEntities.add(lEntity);
        }
        return foundEntities;
    }

    public List<LivingEntity> findValidVehicle(List<Entity> entities) {
        List<LivingEntity> foundEntities = new ArrayList<>();

        for (Entity entity : entities) {
            // Filter unneeded entities depending on the configuration
            if (brainWash && !(entity instanceof Animals)) continue;
            if (!brainWash && !(entity instanceof Chicken)) continue;
            Animals animal = (Animals) entity;

            // Tamed animals have a stronger mind and can resist the brainwashing.
            if (animal instanceof Tameable && brainWash) {
                Tameable tameable = (Tameable) animal;
                if (tameable.isTamed()) continue;
            }
            // Check if the entity is restricted in some matter.
            if (!isValidEntity(animal)) continue;

            foundEntities.add(animal);
        }
        return foundEntities;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onIntervalEvent(IntervalEvent event) {
        for (Player player : CORE.getServer().getOnlinePlayers()) {
            List<Entity> allNearbyEntity = player.getNearbyEntities(searchRadius, searchRadius, searchRadius);
            List<LivingEntity> nearbyRiders = findValidRider(allNearbyEntity);
            for (LivingEntity entity : nearbyRiders) {

                // Prevent mounting every interval
                if (mountProbability < Math.random()) continue;

                // Lookup nearby entities who are mountable.
                List<Entity> inMountRadius = entity.getNearbyEntities(mountRadius, mountRadius, mountRadius);
                List<LivingEntity> mountAble = findValidVehicle(inMountRadius);

                // Don't attempt to mount when there are no mountable entities nearby.
                if (mountAble.isEmpty()) continue;
                int vehicle = (int) (Math.random() * mountAble.size());
                mountAble.get(vehicle).addPassenger(entity);

            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTargetUpdate(EntityTargetEvent event) {
        if (!brainWash) return; // Only update anger when brainwash is enabled

        // Only update anger when zombies change their target and the target is alive.
        if (!(event.getEntity() instanceof Zombie && event.getTarget() instanceof LivingEntity)) return;
        Zombie entity = (Zombie) event.getEntity();
        LivingEntity target = (LivingEntity) event.getTarget();

        // Update the anger of the vehicle if they are riding an animal.
        if (!entity.isInsideVehicle()) return;
        if (!(entity.getVehicle() instanceof Animals)) return;
        Animals vehicle = (Animals) entity.getVehicle();
        vehicle.setTarget(target);
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        searchRadius = config.getInt("searchRadius", 10);
        mountRadius = config.getInt("mountFindRange", 2);
        mountProbability = config.getDouble("mountPercentage", 0) / 100;

        brainWash = config.getBoolean("canBrainWash", false);


    }
}
