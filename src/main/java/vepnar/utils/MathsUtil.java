package vepnar.utils;

import org.bukkit.Location;

public class MathsUtil {

    /**
     * Get locations in a circle around a given location.
     *
     * @param center of the circle
     * @param radius radius of the circle
     * @param steps  amount of steps in the circle
     * @return list with new locations
     */
    public static Location[] getArcSpots(Location center, double radius, int steps) {
        Location[] dots = new Location[steps];
        double x = center.getX();
        double y = center.getY();
        double z = center.getZ();

        for (int i = 0; i < steps; i++) {
            double t = 2 * Math.PI * i / steps;
            double nx = Math.round(x + radius * Math.cos(t));
            double nz = Math.round(z + radius * Math.sin(t));
            dots[i] = new Location(center.getWorld(), nx, y, nz);

        }
        return dots;
    }

    public static int naturalDecay(int maxDuration, double distance, double decay) {
        double distanceDecay = Math.exp(-1 * (distance * decay));
        return (int) (maxDuration * distanceDecay);
    }
}
