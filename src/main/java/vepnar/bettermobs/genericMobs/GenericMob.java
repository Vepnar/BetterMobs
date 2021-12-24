package vepnar.bettermobs.genericMobs;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import vepnar.bettermobs.Main;

import java.io.File;

public class GenericMob implements IMobListener {

    protected final Main CORE;
    protected final String NAME;
    protected final int VERSION;

    protected YamlConfiguration config;
    protected boolean enabled = false;

    public GenericMob(Main javaPlugin, String name, int version) {
        CORE = javaPlugin;
        NAME = name;
        VERSION = version;
    }

    private File getConfigFile() {
        String configName = "/" + getName() + ".yml";

        File folder = new File(CORE.getDataFolder().getPath() + "/mobs/");
        if (!folder.exists())
            folder.mkdir();

        return new File(folder + configName);
    }

    private boolean setDefaultConfig() {
        // Create path to desired configuration file
        File configFile = getConfigFile();
        try {
            if (!configFile.exists()) {
                // Attempt to create a configuration file.
                CORE.saveResource("mobs/" + getName() + ".yml", false);
                CORE.debug(getName() + " config has been created.");
            }
            return true;
        } catch (Exception e) {
            CORE.getLogger().warning(getName() + " config can't be created.");
            CORE.getLogger().warning(e.getMessage());
            return false;
        }

    }

    @Override
    public void reloadConfig() {
        // newState is the state the config should be updated to.
        boolean newState = setDefaultConfig();

        // When an attempt to create a new config fails the module should be disabled.

        // Read the configuration file
        config = YamlConfiguration.loadConfiguration(getConfigFile());

        // Check config version.
        int configVersion = config.getInt("version", 0);
        if (configVersion != VERSION) {
            newState = false;
            CORE.getLogger().warning(this.getName() + " Config version don't match up. Expected: `" + VERSION + "` got: `" + configVersion + "`");
        }

        // Check if the plugin is enabled in the settings.
        if (!config.getBoolean("enabled", false))  newState = false;

        CORE.debug(getName() + " state has been updated from: `" + this.enabled + "`, to: `" + newState + "`");
        if (newState && !this.enabled) {
            this.enable();
        } else if (!newState && this.enabled) {
            this.disable();
        }

    }

    @Override
    public void initialize() {
        reloadConfig();
        if(enabled) {
            CORE.debug(getName() + " Loaded");
        }else {
            CORE.debug(getName() + " Not loaded");
        }
    }

    @Override
    public void enable() {
        enabled = true;
        CORE.getServer().getPluginManager().registerEvents(this, CORE);
    }

    @Override
    public void disable() {
        enabled = false;
        HandlerList.unregisterAll(this);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
