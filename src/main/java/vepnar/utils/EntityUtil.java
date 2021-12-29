package vepnar.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EntityUtil {

    public static long getLong(Entity entity, String key, long fallback) {
        if (entity.hasMetadata(key)) {
            return entity.getMetadata(key).get(0).asLong();
        } else {
            return fallback;
        }
    }

    public static boolean isPlayerNearby(Entity entity, int radius) {
        return entity.getNearbyEntities(radius, radius, radius).stream().anyMatch(e -> e instanceof Player);
    }

    public static List<Player> getNearbyPlayers(Entity entity, int radius) {
        final List<Player> players = new ArrayList<>();
        for (Entity surrounding : entity.getNearbyEntities(radius, radius, radius)) {
            if (surrounding instanceof Player) {
                players.add((Player) surrounding);
            }
        }
        return players;
    }

    public static double distanceBetweenEntities(Entity ent1, Entity ent2) {
        return ent1.getLocation().distance(ent2.getLocation());
    }
}
