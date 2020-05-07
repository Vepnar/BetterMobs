package vepnar.bettermobs.events;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.util;

public class EnderDragonRain implements EventClass {
	int chance, speed, strength, range;
	boolean warning;
	Main m;

	/**
	 * Get configuration data of the configuration file, also return the title of
	 * this handler.
	 * 
	 * @param JavaPlugin
	 * @return title of this event in the configuration file
	 */
	@Override
	public String configName(Main m) {
		this.m = m;
		chance = m.getConfig().getInt("enderDragonRain.attackChance");
		strength = m.getConfig().getInt("enderDragonRain.attackStrength");
		speed = m.getConfig().getInt("enderDragonRain.attackSpeed");
		range = m.getConfig().getInt("enderDragonRain.attackRange");
		warning = m.getConfig().getBoolean("enderDragonRain.attackWarning");
		return "enderDragonRain";
	}

	/**
	 * Handle all events for this event
	 */
	@Override
	public void callEvent(Event e) {
		// Run the random number generator and proceed when all stars align.
		if (util.random(1, chance) != 1)
			return;

		EnderDragonChangePhaseEvent event = (EnderDragonChangePhaseEvent) e;
		Location center = util.getEndPortal(event.getEntity());

		// Cancel when the portal is not chuck loaded.
		if (center == null)
			return;

		// Get all players in range.
		List<LivingEntity> players = util.filterPlayers(center, range);

		// Generate random strength.
		int randomStrength = util.random(1, strength) + 2;

		// Start the attack interval.
		new DragonTimer(players, randomStrength).runTaskTimer(m, 0L, speed);

		if (!warning)
			return;

		// Give all players temporary blindness.
		for (LivingEntity livingentity : players) {
			if (livingentity.hasPotionEffect(PotionEffectType.BLINDNESS))
				continue;
			livingentity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1), false);

		}

	}

	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof EnderDragonChangePhaseEvent;
	}
}

class DragonTimer extends BukkitRunnable {

	private int counter;
	private List<LivingEntity> players;

	public DragonTimer(List<LivingEntity> players, int counter) {
		this.players = players;
		this.counter = counter;

	}

	@Override
	public void run() {
		for (LivingEntity player : players) {

			// Create a new location and angle.
			Location loc = player.getLocation();
			loc.setY(120d);
			loc.setPitch(90f);

			// Check if the location is chunk loaded
			if (util.shouldSpawn(loc, 1, 1) == null)
				continue;

			// Spawn the dragon fireball.
			player.getWorld().spawnEntity(loc, EntityType.DRAGON_FIREBALL);

		}
		counter--;
		if (counter == 0)
			this.cancel();
	}
}
