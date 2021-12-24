package vepnar.bettermobs.genericMobs;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import vepnar.bettermobs.IntervalEvent;
import vepnar.bettermobs.Main;

import java.util.ArrayList;
import java.util.List;

public class GenericMountMob extends GenericMob {

    private double mountProbability;
    private int searchRadius;
    private int mountRadius;

    public GenericMountMob(Main javaPlugin, String name, int version) {
        super(javaPlugin, name, version);
    }

    protected boolean isInvalidEntity(LivingEntity entity) {
        return entity.isInsideVehicle() && !entity.hasAI() && !entity.isEmpty() && entity.isLeashed() && entity.getFireTicks() != 0;
    }

    public List<LivingEntity> findValidRider(List<Entity> entities) {
        List<LivingEntity> filteredEntities = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) entity;
                if (isInvalidEntity(living)) continue;
                filteredEntities.add((LivingEntity) entity);
            }
        }
        return filteredEntities;
    }

    public List<LivingEntity> findValidVehicle(List<Entity> entities) {
        return findValidRider(entities);
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

    public void reloadConfig() {
        super.reloadConfig();
        searchRadius = config.getInt("searchRadius", 10);
        mountRadius = config.getInt("mountFindRange", 2);
        mountProbability = config.getDouble("mountPercentage", 0) / 100;
    }

}
