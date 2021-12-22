package vepnar.bettermobs.genericMobs;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import vepnar.bettermobs.Main;

import java.io.File;

public class GenericMob implements IMobListener {

    protected final Main core;
    protected final int VERSION = 1;
    protected YamlConfiguration config;
    protected boolean enabled = false;

    public GenericMob(Main javaPlugin) {
        core = javaPlugin;
    }

    private File getConfigFile() {
        String configName = "/" + getName() + ".yml";

        File folder = new File(core.getDataFolder().getPath() + "/mobs/");
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
                core.saveResource("mobs/" + getName() + ".yml", false);
                core.debug(getName() + " config has been created.");
            }
            return true;
        } catch (Exception e) {
            core.getLogger().warning(getName() + " config can't be created.");
            core.getLogger().warning(e.getMessage());
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
            core.getLogger().warning(this.getName() + " Config version don't match up. Expected: `" + VERSION + "` got: `" + configVersion + "`");
        }

        // Check if the plugin is enabled in the settings.
        if (!config.getBoolean("enabled", false))  newState = false;

        core.debug(getName() + " state has been updated from: `" + this.enabled + "`, to: `" + newState + "`");
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
            core.debug(getName() + " Loaded");
        }else {
            core.debug(getName() + " Not loaded");
        }
    }

    @Override
    public void enable() {
        enabled = true;

    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "GenericMob";
    }
}
