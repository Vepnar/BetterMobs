/**
 * Here all events will be handled.
 * 
 * @version 1.0
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
	int tick = 50;

	/**
	 * Initialize the Bettermobs event listener
	 * 
	 * @param m the JavaPlugin class or main file
	 */
	public MobListener(Main m) {

		javaplugin = m;
	}

	/**
	 * Handle all PlayerMoveEvent's. and call events who are enabled in the
	 * configuration file. Only do this rarely because this is quite CPU heavy when
	 * there are a lot of active players.
	 */
	@EventHandler
	public void onEntityInteractEvent(PlayerMoveEvent e) {
		int rand = (int) (Math.random() * tick); // Reduce lag
		if (rand != 1 || javaplugin.listen)
			return;

		for (EventClass event : javaplugin.eventList) {
			if (e.isCancelled())
				break;
			if (!event.canBeCalled(e))
				continue;
			event.callEvent(e);
		}
		tick = javaplugin.getConfig().getInt("entityUpdateSpeed");
	}

	/**
	 * Handle all entity spawn events. Don't call this function yourself!
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
	 * Handle all projectile launch events. Don't call this function yourself!
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
	 * Handle all dragon phases events. Don't call this function yourself!
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
	 * Handle entity death events. Don't call this function yourself!
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
	 * Handle entity by entity damage events.
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
