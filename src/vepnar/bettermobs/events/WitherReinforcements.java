package vepnar.bettermobs.events;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.util;

public class WitherReinforcements implements EventClass {
	int spawnradius, cooldown, spawnamount, spawnchance;

	/**
	 * Receive configuration name and check if this event is enabled.
	 */
	@Override
	public String configName(Main m) {
		spawnamount = m.getConfig().getInt("witherReinforcements.spawnAmount");
		cooldown = m.getConfig().getInt("witherReinforcements.cooldown") * 20;
		spawnradius = m.getConfig().getInt("witherReinforcements.spawnRadius");
		spawnchance = m.getConfig().getInt("witherReinforcements.spawnChance");
		return "witherReinforcements";
	}

	/**
	 * Check if this class is compatible with the given event.
	 * 
	 * @param e event that should be checked.
	 * @return true when it is compatible and false when it is not.
	 */
	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof ProjectileLaunchEvent;
	}

	@Override
	public void callEvent(Event e) {
		ProjectileLaunchEvent event = (ProjectileLaunchEvent) e;
		// Check if shot entity is a wither skull.
		if (event.getEntity() instanceof WitherSkull) {
			WitherSkull wSkull = (WitherSkull) event.getEntity();

			// Apply random chance.
			if (util.random(1, spawnchance) != 1)
				return;

			// Check if shot by wither and if it is a charged skull
			if (!(wSkull.getShooter() instanceof Wither || wSkull.isCharged()))
				return;
			Wither wither = (Wither) wSkull.getShooter();
			if (wither.hasPotionEffect(PotionEffectType.LUCK))
				return;

			// Create random values.
			int random_radius = (int) (Math.random() * spawnradius) + 2;
			int random_amount = (int) (Math.random() * spawnamount) + 1;

			// Make a circle and loop through locations.
			Location[] spawnLocations = util.getArcSpots(wither.getLocation(), random_radius, random_amount);
			for (Location spawnLocation : spawnLocations) {

				// Check if the wither skeleton can spawn.
				spawnLocation = util.shouldSpawn(spawnLocation, random_radius, 2);
				if (spawnLocation == null)
					continue;

				// Spawn a wither skeleton.
				spawnLocation.getWorld().spawnParticle(Particle.FLAME, spawnLocation, 60);
				spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.WITHER_SKELETON);

			}
			// Add cooldown
			wither.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, cooldown, 0), false);

		}

	}
}
