/**
 * Main handler for the BetterMobs plugin.
 * Here all the default values and commands will be processed and/or initialized.
 * 
 * @version 1.1
 * @author Arjan de Haan (Vepnar)
 */
package vepnar.bettermobs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteStreams;

import vepnar.bettermobs.commands.AuthorCommand;
import vepnar.bettermobs.commands.ReloadCommand;
import vepnar.bettermobs.events.*;

public class Main extends JavaPlugin {

	public boolean listen = true;
	public String prefix = "§7[§cBetterMobs§7]§f ";

	List<EventClass> eventList = new ArrayList<EventClass>();
	List<EventClass> unusedEventList = new ArrayList<EventClass>();

	@Override
	public void onEnable() {

		// Load configuration
		loadDefaultConfig();

		// Load commands
		initializeCommands();

		// Load events
		initializeEvents();
		enableEvents();

	}

	@Override
	public void onDisable() {
		// Destroy every registered event and disable every listener.
		eventList.clear();
		unusedEventList.clear();
		listen = true;

	}
	
	/**
	 * Attempt to create a configuration file in BetterMobs directory when there is no configuration file.
	 */
	public void loadDefaultConfig() {

		// Create the BetterMobs directory when it does not exist.
		File folder = getDataFolder();
		if (!folder.exists())
			folder.mkdir();
		
		// Access configuration file.
		File resourceFile = new File(folder, "config.yml");
		
		// Attempt to write to the configuration file.
		try {
			if (!resourceFile.exists()) {
				resourceFile.createNewFile();
				try (InputStream in = getResource("config.yml");
						OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}
				
				// Report on our success.
				getLogger().info("Config successfully created!");
			}
		} catch (Exception e) {
			// Report the issue to the console.
			getLogger().warning("Can't create config file");
			e.printStackTrace();
		}
	}

	/**
	 * Register command handlers.
	 */
	public void initializeCommands() {
		this.getCommand("bettermobs-reload").setExecutor(new ReloadCommand(this));
		this.getCommand("bettermobs-author").setExecutor(new AuthorCommand(this));
	}

	/**
	 * Handle the initialization of all the events.
	 * The order of initialization is really important. 
	 */
	public void initializeEvents() {
		
		// Register general event listener
		getServer().getPluginManager().registerEvents(new MobListener(this), this);

		// Initialize all the event classes.
		unusedEventList.add(new SkeletonSwordSwitch());
		unusedEventList.add(new WSkeletonSwordSwitch());
		unusedEventList.add(new SkeletonSpiderMount());
		unusedEventList.add(new ZombieChickenMount());
		unusedEventList.add(new ChargedCreeperSpawner());
		unusedEventList.add(new CreeperSpawnPotionEffects());
		unusedEventList.add(new WitchNecromancer());
		unusedEventList.add(new WitherReinforcements());
		unusedEventList.add(new WitherEffects());
		unusedEventList.add(new BabyEnderDragons());
		unusedEventList.add(new EnderDragonMites());
		unusedEventList.add(new NoDragonPerching());

	}

	/**
	 * Used to unload and load all events previously registered.
	 * @see initializeEvents for registering events.
	 */
	public void enableEvents() {
		// Clear event list.
		eventList.clear();
		
		// Load new events.
		for (EventClass event : unusedEventList) {
			
			// Check if the given event is allowed to run and add it to the list if it is.
			if (this.getConfig().getBoolean(event.configName(this) + ".enabled"))
				eventList.add(event);
		}
		
		// Enable the listener.
		listen = false;
	}

}
