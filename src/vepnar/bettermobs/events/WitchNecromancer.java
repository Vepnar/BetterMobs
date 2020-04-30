package vepnar.bettermobs.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.util;

public class WitchNecromancer implements EventClass {
	int spawnradius = 5;
	int cooldown = 450;
	int spawnamount = 3;
	int scanradius = 10;

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
		return e instanceof PlayerMoveEvent;
	}
	
	/**
	 * Handle given event. this should only be called from the event handler.
	 * 
	 * @param e event that should be handled
	 */
	@Override
	public void callEvent(Event e) {
		PlayerMoveEvent event = (PlayerMoveEvent) e;
		Player player = event.getPlayer();
		long time = player.getWorld().getTime();
		
		// Only spawn baby zombies at night.
		if (!(time < 12300 || time > 23850)) return;
		
		// Find entities and loop through them.
		List<LivingEntity> entities = filterEntities(player.getNearbyEntities(scanradius, scanradius, scanradius));
		for(LivingEntity  entity : entities) {
			
			// Create random values.
			int random_radius = (int) (Math.random() * spawnradius) + 2;
			int random_amount = (int) (Math.random() * spawnamount) + 1;
			
			// Make a circle and loop through locations.
			Location[] spawnLocations = util.getArcSpots(entity.getLocation(), random_radius, random_amount);
			for (Location spawnLocation : spawnLocations) {
				
				// Check if the zombie can spawn.
				spawnLocation = util.shouldSpawn(spawnLocation, random_radius, 2);
				if (spawnLocation == null) continue;
				
				// Spawn a baby zombie.
				Zombie monster = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
				monster.setBaby(true);
			}
			
			// Add potion effect to the witch.
			entity.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, cooldown, 1), false);
		}
		
		
	}
	
	/**
	 * Filter all unused entities and witches with the luck effect.
	 * @param entities List of entities close to a location
	 * @return A list of witches without luck
	 */
	public List<LivingEntity> filterEntities(List<Entity> entities) {

		List<LivingEntity> lEntities = new ArrayList<LivingEntity>();
		for (Entity entity : entities)
			if(entity.getType() == EntityType.WITCH) { 
				LivingEntity witch = (LivingEntity) entity;
				if(!(witch.hasPotionEffect(PotionEffectType.LUCK)))
					lEntities.add(witch);
			}
		return lEntities;
			
	}
	
}
