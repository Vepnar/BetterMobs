package vepnar.bettermobs.listeners;

import org.bukkit.entity.*;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMountMob;

import java.util.ArrayList;
import java.util.List;

public class DominantSkeleton extends GenericMountMob {
    boolean listenWitherSkeleton;
    boolean listenCaveSpiders;

    public DominantSkeleton(Main javaPlugin) {
        super(javaPlugin, "DominantSkeleton", 1);
    }

    @Override
    public List<LivingEntity> findValidRider(List<Entity> entities) {
        List<LivingEntity> foundEntities = new ArrayList<>();

        for (Entity entity : entities) {
            // Filter unneeded entities
            if (!(entity instanceof Skeleton)) continue;

            // Don't listen to WitherSkeletons if disabled.
            if (entity instanceof WitherSkeleton && !listenWitherSkeleton) continue;

            LivingEntity lEntity = (LivingEntity) entity;

            // Check if the entity is restricted in some matter.
            if (isInvalidEntity(lEntity)) continue;

            foundEntities.add(lEntity);
        }
        return foundEntities;
    }

    @Override
    public List<LivingEntity> findValidVehicle(List<Entity> entities) {
        List<LivingEntity> foundEntities = new ArrayList<>();

        for (Entity entity : entities) {
            // Filter unneeded entities
            if (!(entity instanceof Spider)) continue;

            // Don't listen to CaveSpiders if disabled.
            if (entity instanceof CaveSpider && !listenCaveSpiders) continue;
            LivingEntity lEntity = (LivingEntity) entity;

            // Check if the entity is restricted in some matter.
            if (isInvalidEntity(lEntity)) continue;

            foundEntities.add(lEntity);
        }
        return foundEntities;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        listenWitherSkeleton = config.getBoolean("includeWitherSkeletons", false);
        listenCaveSpiders = config.getBoolean("includeCaveSpiders", false);


    }
}
