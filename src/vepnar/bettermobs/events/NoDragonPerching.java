package vepnar.bettermobs.events;

import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;

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
		if (event.getCurrentPhase() == Phase.FLY_TO_PORTAL) {
			event.setNewPhase(Phase.CIRCLING);
		}

	}

	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof EnderDragonChangePhaseEvent;
	}

}
