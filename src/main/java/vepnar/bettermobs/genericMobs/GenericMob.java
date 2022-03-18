package vepnar.bettermobs.genericMobs;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import vepnar.bettermobs.Main;

import java.io.File;

public abstract class GenericMob implements Listener {

    protected final Main CORE;
    protected final String NAME;
    protected final int VERSION;
    protected final int API_VERSION;

    protected YamlConfiguration config;
    protected boolean enabled = false;

    public GenericMob(Main javaPlugin, String name, int version, int apiVersion) {
        CORE = javaPlugin;
        NAME = name;
        VERSION = version;
        API_VERSION = apiVersion;
    }

    /**
     *
     * @param probability
     * @return
     */
    protected boolean shouldOccur(double probability) {
        // This function is implemented such the sonarlinter doesn't warn us everytime we use `math.random`.
        return probability > Math.random();
    }

    /**
     * Check if this feature is compatible with the current server minecraft version.
     * @return True if compatible.
     */
    public boolean isCompatible() {
        return Main.API_VERSION >= API_VERSION;
    }

    /**
     * Create the required directories for the configuration file.
     * And create a configuration file object.
     * @return Configuration file.
     */
    private File getConfigFile() {
        String configName = "/" + getName() + ".yml";

        File folder = new File(CORE.getDataFolder().getPath() + "/mobs/");
        if (!folder.exists())
            folder.mkdir();

        return new File(folder + configName);
    }

    /**
     * Copy the default configuration from the jar to the plugin directory.
     * @return True if successful & false if failed.
     */
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

    /**
     * Read feature specific configuration.
     */
    public abstract void readConfig();

    /**
     * Read general mob listener configuration.
     * And update the state of the feature accordingly.
     */
    public void initializeConfig() {
        // newState is the state the config should be updated to.
        boolean newState = setDefaultConfig();

        // When an attempt to create a new config fails the module should be disabled.

        // Read the configuration file
        config = YamlConfiguration.loadConfiguration(getConfigFile());

        // Check config version.
        int configVersion = config.getInt("version", 0);
        if (configVersion != VERSION) {
            newState = false;
            CORE.getLogger().warning(this.getName() + " config version don't match up. Expected: `" + VERSION + "` got: `" + configVersion + "`");
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

    /**
     * Read general mob configuration.
     * Child abstract classes should also read specific class configuration here and not in readConfig.
     */
    public void reloadConfig() {
        initializeConfig();
        readConfig();
    }

    /**
     * Load the configuration & enable the listener if the feature is enabled.
     */
    @SuppressWarnings("unused")
    public void initialize() {
        reloadConfig();
        if(enabled) {
            CORE.debug(getName() + " Loaded");
        }else CORE.debug(getName() + " Not loaded");
    }

    /**
     * Register the current listener into the server.
     */
    public void enable() {
        enabled = true;
        CORE.getServer().getPluginManager().registerEvents(this, CORE);
    }

    /**
     * Deregister this mob listener.
     */
    public void disable() {
        enabled = false;
        HandlerList.unregisterAll(this);
    }

    /**
     * Check if the current feature is enabled.
     * @return True if enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Retrieve the specific name of this feature.
     * @return Name of the feature.
     */
    public String getName() {
        return NAME;
    }
}
