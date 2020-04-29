package vepnar.bettermobs.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

public class ZombieChickenMount extends SkeletonSpiderMount {
	/**
	 * Receive configuration name and check if this event is enabled.
	 */
	@Override
	public String configName() {
		return "zombieChickenMount";
	}
	
	/**
	 * Filter all entities that can mount another entity and ain't riding on another entity
	 * 
	 * @param entities you want to be filtered
	 * @return List of living entities filtered
	 */
	@Override
	public List<LivingEntity> filterPassengerEntities(List<Entity> entities) {

		List<LivingEntity> lEntities = new ArrayList<LivingEntity>();
		for (Entity entity : entities)
			if (entity instanceof Zombie)
				if(((Zombie) entity).isBaby() && !entity.isInsideVehicle()) 
					lEntities.add((LivingEntity) entity);
		return lEntities;
			
	}
	
	/**
	 * Find rideable entities close to the zombie with currently no passengers.
	 * 
	 * @param entities list of entities close to the baby zombie.
	 * @return entity where a baby zombie could ride on.
	 */
	@Override
	public Entity filterMountableEntities(List<Entity> entities) {
		for (Entity entity : entities)
				if(entity instanceof Chicken && entity.getPassengers().isEmpty()) 
						return entity;
		return null;
	}

}
