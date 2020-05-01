package vepnar.bettermobs.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.util;

public class WitchNecromancer implements EventClass {
	int spawnradius, cooldown, spawnamount, scanradius;
	final String companionname = "§c§lWitch companion";
	final EntityType companion = EntityType.ZOMBIE;

	/**
	 * Receive configuration name and check if this event is enabled.
	 */
	@Override
	public String configName(Main m) {
		spawnamount = m.getConfig().getInt("witchNecromancer.spawnAmount");
		cooldown = m.getConfig().getInt("witchNecromancer.cooldown") * 20;
		spawnradius = m.getConfig().getInt("witchNecromancer.spawnRadius");
		scanradius = m.getConfig().getInt("witchNecromancer.scanRadius");
		return "witchNecromancer";
	}

	/**
	 * Check if this class is compatible with the given event.
	 * 
	 * @param e event that should be checked.
	 * @return true when it is compatible and false when it is not.
	 */
	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof PlayerMoveEvent || e instanceof EntityDeathEvent;
	}

	/**
	 * Handle given event. this should only be called from the event handler.
	 * 
	 * @param e event that should be handled
	 */
	@Override
	public void callEvent(Event e) {
		if (e instanceof EntityDeathEvent) {
			deathEvent((EntityDeathEvent) e);

		} else if (e instanceof PlayerMoveEvent)
			onPlayerMove((PlayerMoveEvent) e);

	}

	/**
	 * Handle the death of the summoned zombies.
	 */
	public void deathEvent(EntityDeathEvent e) {
		if (util.checkCompanion(e.getEntity(), companion, companionname))
			e.getDrops().clear();

	}

	/**
	 * Handle player move events.
	 */
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		long time = player.getWorld().getTime();

		// Only spawn baby zombies at night.
		if (time < 12300 || time > 23850)
			return;

		// Find entities and loop through them.
		List<LivingEntity> entities = filterEntities(player.getNearbyEntities(scanradius, scanradius, scanradius));
		for (LivingEntity entity : entities) {

			// Create random values.
			int randomRadius = (int) (Math.random() * spawnradius) + 2;
			int randomAmount = (int) (Math.random() * spawnamount) + 1;

			// Make a circle and loop through locations.
			Location[] spawnLocations = util.getArcSpots(entity.getLocation(), randomRadius, randomAmount);
			for (Location spawnLocation : spawnLocations) {

				// Check if the zombie can spawn.
				spawnLocation = util.shouldSpawn(spawnLocation, randomRadius, 2);
				if (spawnLocation == null)
					continue;

				// Spawn a baby zombie.
				Zombie monster = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, companion);
				spawnLocation.getWorld().spawnParticle(Particle.FLAME, spawnLocation, 60);
				monster.setCustomName(companionname);
				monster.setCustomNameVisible(false);
				monster.setBaby(true);
			}

			// Add potion effect to the witch.
			entity.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, cooldown, 1), false);
		}
	}

	/**
	 * Filter all unused entities and witches with the luck effect.
	 * 
	 * @param entities List of entities close to a location
	 * @return A list of witches without luck
	 */
	public List<LivingEntity> filterEntities(List<Entity> entities) {

		List<LivingEntity> lEntities = new ArrayList<LivingEntity>();
		for (Entity entity : entities)
			if (entity.getType() == EntityType.WITCH) {
				LivingEntity witch = (LivingEntity) entity;
				if (!(witch.hasPotionEffect(PotionEffectType.LUCK)))
					lEntities.add(witch);
			}
		return lEntities;

	}

}
