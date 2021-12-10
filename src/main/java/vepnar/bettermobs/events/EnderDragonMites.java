package vepnar.bettermobs.events;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vepnar.bettermobs.Main;
import vepnar.bettermobs.util;

public class EnderDragonMites extends BabyEnderDragons {

	final EntityType companion = EntityType.ENDERMITE;
	final String companionname = "§c§lDragon Mites";

	/**
	 * Get configuration data of the configuration file, also return the title of
	 * this handler.
	 * 
	 * @param JavaPlugin
	 * @return title of this event in the configuration file
	 */
	@Override
	public String configName(Main m) {
		radius = m.getConfig().getInt("enderDragonMites.spawnRadius");
		chance = m.getConfig().getInt("enderDragonMites.spawnChance");
		amount = m.getConfig().getInt("enderDragonMites.spawnAmount");
		damagebooster = m.getConfig().getInt("enderDragonMites.damageBooster");
		regen = m.getConfig().getBoolean("enderDragonMites.dragonRegen");
		return "enderDragonMites";

	}

	/*
	 * Handle spawn moments
	 */
	public void DragonPhaseEvent(EnderDragonChangePhaseEvent e) {
		// Check if the dragon is in the correct phase

		// Calculate chance.

		if (util.random(0, chance) != 1)
			return;

		// Generate random values.
		int randomRadius = (int) (Math.random() * radius) + 5;
		int randomAmount = (int) (Math.random() * amount) + 1;
		Location portalLocation = util.getEndPortal(e.getEntity());
		
		// Cancel when the chunk is not loaded.
		if(portalLocation == null)
			return;

		// Check if there are players in range
		if (util.filterPlayers(portalLocation, 150).size() == 0)
			return;

		Location[] spawnLocations = util.getArcSpots(portalLocation, randomRadius, randomAmount);
		for (Location spawnLocation : spawnLocations) {

			// Check if the baby dragon can spawn.
			spawnLocation = util.shouldSpawn(spawnLocation, randomRadius, 1);
			if (spawnLocation == null)
				continue;

			// Spawn particles for the new entities.
			spawnLocation.getWorld().spawnParticle(Particle.DRAGON_BREATH, spawnLocation, 60);

			// Spawn phantoms with names and special effects
			LivingEntity monster = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, companion);
			monster.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, damagebooster), false);
			monster.setCustomName(companionname);
			monster.setCustomNameVisible(false);
		}
	}

}
