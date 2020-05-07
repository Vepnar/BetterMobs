package vepnar.bettermobs.events;

import org.bukkit.entity.EntityType;

import vepnar.bettermobs.Main;

public class CaveSpiderSpawn extends IllusionerSpawn {
	int chance;
	final EntityType target = EntityType.SPIDER;
	final EntityType replace = EntityType.CAVE_SPIDER;

	/**
	 * Get configuration data of the configuration file, also return the title of
	 * this handler.
	 * 
	 * @param JavaPlugin
	 * @return title of this event in the configuration file
	 */
	@Override
	public String configName(Main m) {
		chance = m.getConfig().getInt("caveSpiderSpawn.spawnChance");
		return "caveSpiderSpawn";
	}
}
