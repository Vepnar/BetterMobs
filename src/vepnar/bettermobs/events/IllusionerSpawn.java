package vepnar.bettermobs.events;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.util;

public class IllusionerSpawn implements EventClass {
	int chance;
	final EntityType target = EntityType.WITCH;
	final EntityType replace = EntityType.ILLUSIONER;

	/**
	 * Get configuration data of the configuration file, also return the title of
	 * this handler.
	 * 
	 * @param JavaPlugin
	 * @return title of this event in the configuration file
	 */
	@Override
	public String configName(Main m) {
		chance = m.getConfig().getInt("illusionerSpawn.spawnChance");
		return "illusionerSpawn";
	}

	@Override
	public void callEvent(Event e) {
		CreatureSpawnEvent event = (CreatureSpawnEvent) e;
		if (event.getSpawnReason() != SpawnReason.NATURAL || event.getEntityType() != target)
			return;

		// Check if the RNG wants us to spawn an illusioner
		if (util.random(0, chance) != 1)
			return;

		// Replace old entity with a new one.
		Location loc = event.getLocation();
		event.getEntity().remove();
		loc.getWorld().spawnEntity(loc, replace);

	}

	/**
	 * Verify if the event type match.
	 */
	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof CreatureSpawnEvent;
	}

}
