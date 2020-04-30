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

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteStreams;

import vepnar.bettermobs.commands.AuthorCommand;
import vepnar.bettermobs.commands.ReloadCommand;
import vepnar.bettermobs.events.*;


public class Main extends JavaPlugin implements CommandExecutor{
	
	public boolean listen = true;
	public String prefix = "§7[§cBetterMobs§7]§f ";
	
	List<EventClass> eventList = new ArrayList<EventClass>();
	List<EventClass> unusedEventList = new ArrayList<EventClass>();
	
	
	/**
	 * This runs when the plugin gets started.
	 * Here we can initialize all settings and load files.
	 */
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
	
	/**
	 * This runs when the plugin gets disabled
	 */
	@Override
	public void onDisable() {
		eventList.clear();
		unusedEventList.clear();
		listen = true;
		
	}
	
	/*
	 * Copy the default config file to the plugin directory.
	 * The default config file will be copied from /src/config.yml to /pluginname/config.yml
	 */
	public void loadDefaultConfig() {

        File folder = getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, "config.yml");
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = getResource("config.yml");
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
                getLogger().info("Config successfully created!");
            }
        } catch (Exception e) {
        	getLogger().warning("Can't create config file");
            e.printStackTrace();
        }
	}
	
	/**
	 * Register all commands and listen to them
	 */
	public void initializeCommands() {
		this.getCommand("bettermobs-reload").setExecutor(new ReloadCommand(this));
		this.getCommand("bettermobs-author").setExecutor(new AuthorCommand(this));
	}
	
	/**
	 * This will create the events and add them to the list of events.
	 */
	public void initializeEvents() {
		getServer().getPluginManager().registerEvents(new MobListener(this), this);
		
		unusedEventList.add(new SkeletonSwordSwitch());
		unusedEventList.add(new WSkeletonSwordSwitch());
		unusedEventList.add(new SkeletonSpiderMount());
		unusedEventList.add(new ZombieChickenMount());
		unusedEventList.add(new ChargedCreeperSpawner());
		unusedEventList.add(new CreeperSpawnPotionEffects());
		unusedEventList.add(new WitchNecromancer());
		unusedEventList.add(new WitherReinforcements());
		
		
	}
	
	/**
	 * This will enable the events when they are enabled in the configuration file.
	 */
	public void enableEvents() {
		eventList.clear();
		for (EventClass event : unusedEventList) {
			if(this.getConfig().getBoolean(event.configName(this) + ".enabled"))
				eventList.add(event);
		}
		listen = false;
	}

}
