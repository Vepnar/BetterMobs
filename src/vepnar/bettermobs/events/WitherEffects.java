package vepnar.bettermobs.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.util;

public class WitherEffects implements EventClass {
	int radius, cooldown, duration, chance;
	final PotionEffectType[] WITHEREFFECTS = { PotionEffectType.HUNGER, PotionEffectType.WEAKNESS,
			PotionEffectType.SLOW_DIGGING, PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION };

	/**
	 * Receive configuration name and check if this event is enabled.
	 */
	@Override
	public String configName(Main m) {
		cooldown = m.getConfig().getInt("witherEffects.cooldown") * 20;
		radius = m.getConfig().getInt("witherEffects.attackRadius");
		duration = m.getConfig().getInt("witherEffects.attackDuration") * 20;
		chance = m.getConfig().getInt("witherEffects.attackChance");
		return "witherEffects";
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

	/**
	 * Handle the event.
	 * 
	 * @param e
	 */
	@Override
	public void callEvent(Event e) {
		ProjectileLaunchEvent event = (ProjectileLaunchEvent) e;
		// Check if shot entity is a wither skull.
		if (event.getEntity() instanceof WitherSkull) {
			WitherSkull wSkull = (WitherSkull) event.getEntity();

			// Apply random chance.
			if (util.random(1, chance) != 1)
				return;

			// Check if it is shot by a wither and the wither has no cooldown.
			if (!(wSkull.getShooter() instanceof Wither))
				return;
			Wither wither = (Wither) wSkull.getShooter();
			if (wither.hasPotionEffect(PotionEffectType.UNLUCK))
				return;

			// Generate random radius and effect.
			int randomRadius = util.random(1, radius) + 2;
			int random_effect = util.random(1, WITHEREFFECTS.length) - 1;

			// Get entities in the radius of the wither.
			List<LivingEntity> entities = filterEntities(
					wither.getNearbyEntities(randomRadius, randomRadius, randomRadius));
			if (entities.size() == 0)
				return;

			// Apply effects.
			for (LivingEntity entity : entities)
				entity.addPotionEffect(new PotionEffect(WITHEREFFECTS[random_effect], duration, 0), true);

			// Make a circle and play particle effects.
			Location[] spawnLocations = util.getArcSpots(wither.getLocation(), randomRadius, 8);
			for (Location spawnLocation : spawnLocations) {

				// Check if the particles should spawn.
				spawnLocation = util.shouldSpawn(spawnLocation, 3, 1);
				if (spawnLocation == null)
					continue;

				// Spawn particles.
				spawnLocation.getWorld().spawnParticle(Particle.SQUID_INK, spawnLocation, 60);

			}
			// Add cooldown to the wither.
			wither.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, cooldown, 0), false);
		}
	}

	/**
	 * Filter only players.
	 * 
	 * @param entities List of entities close to a location.
	 * @return A list of living entities with only players.
	 */
	public List<LivingEntity> filterEntities(List<Entity> entities) {

		List<LivingEntity> lEntities = new ArrayList<LivingEntity>();
		for (Entity entity : entities)
			if (entity.getType() == EntityType.PLAYER)
				lEntities.add((LivingEntity) entity);
		return lEntities;

	}
}
