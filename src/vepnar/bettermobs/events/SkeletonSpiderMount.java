package vepnar.bettermobs.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;

public class SkeletonSpiderMount implements EventClass {
	int updateRange, mountFind;

	/**
	 * Return the configuration name and receive some settings for this event.
	 * dvzsdvsds
	 */
	@Override
	public String configName(Main m) {
		updateRange = m.getConfig().getInt("skeletonSpiderMount.updateEntityfromPlayer");
		mountFind = m.getConfig().getInt("skeletonSpiderMount.mountFindRange");
		return "skeletonSpiderMount";
	}

	/**
	 * Handle given event. this should only be called from the event handler.
	 * 
	 * @param e event that should be handled
	 */
	@Override
	public void callEvent(Event e) {
		PlayerMoveEvent MoveEvent = (PlayerMoveEvent) e;
		Player player = MoveEvent.getPlayer();

		List<LivingEntity> PassengerEntities = filterPassengerEntities(
				player.getNearbyEntities(updateRange, updateRange, updateRange));

		for (LivingEntity entity : PassengerEntities) {
			Entity mount = filterMountableEntities(entity.getNearbyEntities(mountFind, mountFind, mountFind));
			if (mount != null)
				mount.addPassenger(entity);
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
		return e instanceof PlayerMoveEvent;
	}

	/**
	 * Filter all entities that can mount another entity and ain't riding on another
	 * entity
	 * 
	 * @param entities you want to be filtered
	 * @return List of living entities filtered
	 */
	public List<LivingEntity> filterPassengerEntities(List<Entity> entities) {

		List<LivingEntity> lEntities = new ArrayList<LivingEntity>();
		for (Entity entity : entities)
			if (entity instanceof Skeleton && !entity.isInsideVehicle())
				lEntities.add((LivingEntity) entity);
		return lEntities;

	}

	/**
	 * Find mountable entities near the skeletons with no riders.
	 * 
	 * @param entities list of entities near the skeletons.
	 * @return entity where a skeleton could ride on.
	 */
	public Entity filterMountableEntities(List<Entity> entities) {
		for (Entity entity : entities)
			if (entity instanceof Spider && entity.getPassengers().isEmpty())
				return entity;
		return null;
	}
}
