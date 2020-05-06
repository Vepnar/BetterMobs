package vepnar.bettermobs.events;

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
		
		// Check the current phase.
		if (event.getCurrentPhase() == Phase.FLY_TO_PORTAL) {

			// See if there are any players nearby.
			EnderDragon dragon = event.getEntity();
			if (util.filterPlayers(dragon.getLocation(), 64).size() == 0) {
				// Set the phase of the ender dragon to circling. 
				event.setNewPhase(Phase.CIRCLING);
				return;
			}

			// Set a new attack phase.
			if (Math.random() < 0.5) {
				event.setNewPhase(Phase.CHARGE_PLAYER);
			} else
				event.setNewPhase(Phase.STRAFING);

		}

	}

	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof EnderDragonChangePhaseEvent;
	}

}
