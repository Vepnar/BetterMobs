package vepnar.bettermobs;

import org.bukkit.event.Event;

public interface EventClass {
	public String configName(Main m);

	public void callEvent(Event e);

	public boolean canBeCalled(Event e);

}
