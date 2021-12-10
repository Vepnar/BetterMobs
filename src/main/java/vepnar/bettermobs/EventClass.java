package vepnar.bettermobs;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public interface EventClass extends Listener {
	public String configName(Main m);

	public void callEvent(Event e);

	public boolean canBeCalled(Event e);

}
