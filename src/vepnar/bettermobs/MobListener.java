/**
 * Here all events will be handled.
 * 
 * @version 1.4
 * @author Arjan de Haan (Vepnar)
 */
package vepnar.bettermobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
// Events
import org.bukkit.event.player.PlayerMoveEvent;

public class MobListener implements Listener {

	Main javaplugin;
	int tick;
	long lastcheck = System.currentTimeMillis();

	public MobListener(Main m) {

		javaplugin = m;
	}

	@EventHandler
	public void onEntityInteractEvent(PlayerMoveEvent e) {

		// Verify if the given delay has passed and if the event listener is enabled.
		if (System.currentTimeMillis() - lastcheck > tick || javaplugin.listen)
			return;

		// Reset the delay
		lastcheck = System.currentTimeMillis();

		// Loop through all enabled events
		for (EventClass event : javaplugin.eventList) {

			// Interrupt the for-loop when the event has been cancelled.
			if (e.isCancelled())
				break;

			// Verify if this event listens to this kind of events.
			if (event.canBeCalled(e))
				event.callEvent(e);
		}

		// Receive updates from the configuration file.
		tick = javaplugin.getConfig().getInt("entityUpdateSpeed");
	}

	/**
	 * @see onEntityInteractEvent
	 */
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent e) {
		if (javaplugin.listen)
			return;

		for (EventClass event : javaplugin.eventList) {
			if (e.isCancelled())
				break;
			if (!event.canBeCalled(e))
				continue;
			event.callEvent(e);
		}
	}

	/**
	 * @see onEntityInteractEvent
	 */
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e) {
		if (javaplugin.listen)
			return;

		for (EventClass event : javaplugin.eventList) {
			if (e.isCancelled())
				break;
			if (!event.canBeCalled(e))
				continue;
			event.callEvent(e);
		}
	}

	/**
	 * @see onEntityInteractEvent
	 */
	@EventHandler
	public void onDragonChangePhase(EnderDragonChangePhaseEvent e) {
		if (javaplugin.listen)
			return;

		for (EventClass event : javaplugin.eventList) {
			if (e.isCancelled())
				break;
			if (!event.canBeCalled(e))
				continue;
			event.callEvent(e);
		}
	}

	/**
	 * @see onEntityInteractEvent
	 */
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if (javaplugin.listen)
			return;

		for (EventClass event : javaplugin.eventList) {
			if (!event.canBeCalled(e))
				continue;
			event.callEvent(e);
		}
	}

	/**
	 * @see onEntityInteractEvent
	 */
	@EventHandler
	public void onEntityHitEvent(EntityDamageByEntityEvent e) {
		if (javaplugin.listen)
			return;

		for (EventClass event : javaplugin.eventList) {
			if (!event.canBeCalled(e))
				continue;
			event.callEvent(e);
		}
	}

}
