package vepnar.bettermobs.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import vepnar.bettermobs.Main;

import java.util.ArrayList;
import java.util.List;

public class EntityUtil {

    private static Main CORE;

    public static long getLong(Entity entity, String key, long fallback) {
        if (entity.hasMetadata(key) && !entity.isDead()) {
            return entity.getMetadata(key).get(0).asLong();
        } else return fallback;
    }

    public static void setLong(Entity entity, String key, long value) {
        if (entity.isDead()) return;
        entity.setMetadata(key, new FixedMetadataValue(CORE, value));
    }

    public static void setBoolean(Entity entity, String key, boolean value) {
        if (entity.isDead()) return;
        entity.setMetadata(key, new FixedMetadataValue(CORE, value));
    }

    public static boolean getBoolean(Entity entity, String key, boolean fallback) {
        if (entity.hasMetadata(key) || !entity.isDead()) {
            return entity.getMetadata(key).get(0).asBoolean();
        } else return fallback;
    }

    public static boolean isPlayerNearby(Entity entity, int radius) {
        return entity.getNearbyEntities(radius, radius, radius).stream().anyMatch(e -> e instanceof Player);
    }

    public static List<Entity> getNearbyEntitiesOfType(Entity entity, int radius, Class entityType) {
        final List<Entity> entities = new ArrayList<>();
        for (Entity surrounding : entity.getNearbyEntities(radius, radius, radius)) {
            if (entityType.isInstance(surrounding)) {
                entities.add(surrounding);
            }
        }

        return entities;
    }

    public static List<Player> getNearbyPlayers(Entity entity, int radius) {
        final List<Player> players = new ArrayList<>();
        for (Entity surrounding : getNearbyEntitiesOfType(entity, radius, Player.class)) {
            players.add((Player) surrounding);
        }
        return players;
    }

    public static double distanceBetweenEntities(Entity ent1, Entity ent2) {
        return ent1.getLocation().distance(ent2.getLocation());
    }

    public static boolean hasCoolDown(Entity entity, String key, int coolDownDuration) {
        long lastEventCall = System.currentTimeMillis() - EntityUtil.getLong(entity, key, 0);
        return coolDownDuration > lastEventCall;
    }

    public static List<Entity> spawnEntityInCircle(EntityType entityType, Location center, double radius, int entityCount) {
        final List<Entity> entities = new ArrayList<>();
        World world = center.getWorld();
        for (Location point : MathsUtil.getArcSpots(center, radius, entityCount)) {
            if (!world.getBlockAt(point).getType().isAir()) continue;
            Entity spawnedEntity = world.spawnEntity(point, entityType);
            entities.add(spawnedEntity);
        }
        return entities;
    }


    public static boolean isInValidEntity(LivingEntity entity) {
        return entity.isDead() || !entity.hasAI() || entity.isInvulnerable() || !entity.isEmpty() || entity.isCustomNameVisible() || !entity.getPassengers().isEmpty();
    }

    public static List<LivingEntity> getAllLivingEntities(Class entityType) {
        final List<LivingEntity> entities = new ArrayList<>();
        for (World world : CORE.getServer().getWorlds()) {
            for (Entity entity : world.getEntitiesByClasses(entityType)) {
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    if (isInValidEntity(livingEntity)) continue;
                    entities.add(livingEntity);
                }
            }
        }
        return entities;
    }


    public static void reloadAll(Main core) {
        CORE = core;
    }
}
