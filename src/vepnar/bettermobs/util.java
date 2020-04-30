package vepnar.bettermobs;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class util {

	/**
	 * Get locations in a circle around a given location.
	 * 
	 * @param center of the circle
	 * @param radius radius of the circle
	 * @param steps amount of steps in the circle
	 * @return list with new locations
	 */
	public static Location[] getArcSpots(Location center, double radius, int steps) {
		Location[] dots = new Location[steps];
		double x = center.getX();
		double y = center.getY();
		double z = center.getZ();
		
		for (int i=0; i < steps; i++) {
			double t = 2 * Math.PI * i / steps;
			double nx = Math.round(x + radius * Math.cos(t));
			double nz = Math.round(z + radius * Math.sin(t));
			dots[i] = new Location(center.getWorld(), nx, y, nz);

		}
		return dots;
	}
	
	/**
	 * Subtract entities from a list with another list.
	 * 
	 * @param minuend The list of entities you want to subtract from.
	 * @param subtrahend The list of entities you subtract
	 */
	public static void subtractListfromList(List<LivingEntity> minuend, List<LivingEntity> subtrahend) {
		
		for (Iterator<LivingEntity> mIterator = minuend.listIterator(); mIterator.hasNext(); ) {
			LivingEntity entity = mIterator.next();

			if(subtrahend.contains(entity))
				mIterator.remove();
		}
	}
	
	/**
	 * Check if there is space for this mob to spawn.
	 * @param spawn Location of the given mob.
	 * @param height height of the given mob.
	 * @return true when the mob is able to spawn at the given location.
	 */
	public static boolean canSpawn(Location spawn, int height) {
		double starty = spawn.getY();
		for(int i=0; height > i; i++) {
			spawn.setY(starty+i);
			if (!spawn.getBlock().getType().isOccluding())
				return true;
		}
		return false;
	}
	
	/**
	 * Get a height the mob should spawn at
	 * @param spawn Spawn location of the mob.
	 * @param radius radius that should be checked for the mob.
	 * @param height height of the given mob.
	 * @return new location for the mob. or null when there is no location for the mob.
	 */
	public static Location shouldSpawn(Location spawn, int radius, int height) {
		double starty = spawn.getY() - radius - height;
		for (int i=0; height + (radius * 2) > i; i++) {
			spawn.setY(starty + i);
			if (canSpawn(spawn, height))
					return spawn;
		}
		return null;
	}
	
	public static int random(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}
}
