package vepnar.bettermobs.events;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.util;

public class ChargedCreeperSpawner implements EventClass {
	int chance;

	/**
	 * Receive configuration information from the configuration file. And return the
	 * name of this event
	 * 
	 * @param JavaPlugin
	 * @return Name of this event in the configuration file
	 */
	@Override
	public String configName(Main m) {
		chance = m.getConfig().getInt("chargedCreeperSpawn.spawnChance");
		return "chargedCreeperSpawn";
	}

	/**
	 * Handle the event.
	 * 
	 * @param e
	 */
	@Override
	public void callEvent(Event e) {
		CreatureSpawnEvent spawn = (CreatureSpawnEvent) e;
		
		// Check if given entity is a creeper and the spawn reason is natural.
		if (spawn.getSpawnReason() != SpawnReason.NATURAL || spawn.getEntityType() != EntityType.CREEPER)
			return;
		
		// Set powered when the RNG agrees
		if (util.random(0, chance) == 1)
			((Creeper) spawn.getEntity()).setPowered(true);

	}

	/**
	 * Check if this class is compatible with the given event.
	 * 
	 * @param e event that should be checked.
	 * @return true when it is compatible and false when it is not.
	 */
	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof CreatureSpawnEvent;
	}

}
