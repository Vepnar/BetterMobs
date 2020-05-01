package vepnar.bettermobs.events;

import org.bukkit.entity.Creeper;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;

public class ChargedCreeperSpawner implements EventClass {
	int spawnchance;

	/**
	 * Receive configuration information from the configuration file. And return the
	 * name of this event
	 * 
	 * @param JavaPlugin
	 * @return Name of this event in the configuration file
	 */
	@Override
	public String configName(Main m) {
		spawnchance = m.getConfig().getInt("chargedCreeperSpawn.spawnChance");
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
		if (spawn.getEntity() instanceof Creeper) {
			int rand = (int) (Math.random() * spawnchance);
			if (rand == 1)
				((Creeper) spawn.getEntity()).setPowered(true);
		}

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
