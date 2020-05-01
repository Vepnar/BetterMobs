package vepnar.bettermobs.events;

import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.util;

public class NoDragonPerching implements EventClass {

	/**
	 * Receive default name
	 */
	@Override
	public String configName(Main m) {
		return "NoDragonPerching";
	}

	@Override
	public void callEvent(Event e) {
		EnderDragonChangePhaseEvent event = (EnderDragonChangePhaseEvent) e;
		if (event.getNewPhase() == Phase.FLY_TO_PORTAL) {

			// Check if there are players nearby and cancel only when there are no players
			// around.
			EnderDragon dragon = event.getEntity();
			Location portal_location = util.getEndPortal(dragon);
			if (util.filterPlayers(portal_location, 150).size() == 0)
				return;

			// Cancel the new phase
			event.setNewPhase(Phase.CHARGE_PLAYER);
			event.setCancelled(true);

		}

	}

	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof EnderDragonChangePhaseEvent;
	}

}
