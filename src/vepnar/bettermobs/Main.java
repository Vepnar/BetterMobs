/**
 * Main handler for the bettermobs plugin.
 * Here all the default values and commands will be processed and/or initialized.
 * 
 * @version 1.0
 * @author Arjan de Haan (Vepnar)
 */
package vepnar.bettermobs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import vepnar.bettermobs.events.*;


public class Main extends JavaPlugin {
	
	boolean disabled = true;
	List<EventClass> eventList = new ArrayList<EventClass>();
	List<EventClass> unusedEventList = new ArrayList<EventClass>();
	
	/**
	 * This runs when the plugin gets started.
	 * Here we can initialize all settings and load files.
	 */
	@Override
	public void onEnable() {
		loadDefaultConfig();
		initializeEvents();
		enableEvents();
		getServer().getPluginManager().registerEvents(new MobListener(this), this);
		
	}
	
	/**
	 * This runs when the plugin gets disabled
	 */
	@Override
	public void onDisable() {
		eventList.clear();
		unusedEventList.clear();
		disabled = true;
		
	}
	
	/*
	 * Copy the default config file to the plugin directory.
	 * The default config file will be copied from /src/config.yml to /pluginname/config.yml
	 */
	public void loadDefaultConfig() {

        File configFile = new File(getDataFolder(), "config.yml");
        FileConfiguration config = getConfig();
       
        if (!configFile.exists()) {
              getLogger().info("Config file created!");
          }
        saveDefaultConfig();
       
        config.options().copyHeader(true);
        config.options().copyDefaults(true);
       
        saveConfig();
	}
	
	public void initializeEvents() {
		unusedEventList.add(new SkeletonSwordSwitch());
		unusedEventList.add(new WSkeletonSwordSwitch());
	}
	
	public void enableEvents() {
		eventList.clear();
		for (EventClass event : unusedEventList) {
			if(this.getConfig().getBoolean(event.configName()))
				eventList.add(event);
		}
		disabled = false;
	}

}
