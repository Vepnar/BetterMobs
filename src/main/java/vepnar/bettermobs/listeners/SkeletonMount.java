package vepnar.bettermobs.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import vepnar.bettermobs.IntervalEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;

import java.util.ArrayList;
import java.util.List;

public class SkeletonMount extends GenericMob {

    int searchRadius;
    int mountRadius;
    double mountProbability;
    boolean listenWitherSkeleton;
    boolean listenCaveSpiders;

    public SkeletonMount(Main javaPlugin) {
        super(javaPlugin);
    }

    public boolean isValidEntity(LivingEntity entity) {
        return !entity.isInsideVehicle() && entity.hasAI() && entity.isEmpty() && !entity.isLeashed() && entity.getFireTicks() == 0;
    }

    public List<LivingEntity> findValidRider(List<Entity> entities) {
        List<LivingEntity> foundEntities = new ArrayList<>();

        for (Entity entity : entities) {
            // Filter unneeded entities
            if (!(entity instanceof Skeleton)) continue;

            // Don't listen to WitherSkeletons if disabled.
            if (entity instanceof WitherSkeleton && !listenWitherSkeleton) continue;

            LivingEntity lEntity = (LivingEntity) entity;

            // Check if the entity is restricted in some matter.
            if (!isValidEntity(lEntity)) continue;

            foundEntities.add(lEntity);
        }
        return foundEntities;
    }

    public List<LivingEntity> findValidVehicle(List<Entity> entities) {
        List<LivingEntity> foundEntities = new ArrayList<>();

        for (Entity entity : entities) {
            // Filter unneeded entities
            if (!(entity instanceof Spider)) continue;

            // Don't listen to CaveSpiders if disabled.
            if (entity instanceof CaveSpider && !listenCaveSpiders) continue;
            LivingEntity lEntity = (LivingEntity) entity;

            // Check if the entity is restricted in some matter.
            if (!isValidEntity(lEntity)) continue;

            foundEntities.add(lEntity);
        }
        return foundEntities;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onIntervalEvent(IntervalEvent event) {
        for (Player player : core.getServer().getOnlinePlayers()) {
            List<Entity> allNearbyEntity = player.getNearbyEntities(searchRadius, searchRadius, searchRadius);
            List<LivingEntity> nearbyRiders = findValidRider(allNearbyEntity);
            for (LivingEntity entity : nearbyRiders) {

                // Prevent from skeleton mounting spiders every update.
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

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        searchRadius = config.getInt("searchRadius", 10);
        mountRadius = config.getInt("mountFindRange", 2);
        mountProbability = config.getDouble("mountPercentage", 0) / 100;

        listenWitherSkeleton = config.getBoolean("includeWitherSkeletons", false);
        listenCaveSpiders = config.getBoolean("includeCaveSpiders", false);


    }

    @Override
    public String getName() {
        return "SkeletonMount";
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
