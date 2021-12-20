package vepnar.bettermobs.genericMobs;

import com.google.common.io.ByteStreams;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class GenericMob implements IMobListener {

    protected final JavaPlugin core;
    protected final int VERSION = 1;
    protected YamlConfiguration config;
    protected boolean enabled = false;

    public GenericMob(JavaPlugin javaPlugin) {
        core = javaPlugin;
    }

    private File getConfigFile() {
        String configName = "/" + getName() + ".yml";

        File folder = new File(core.getDataFolder().getPath() + "/mobs/");
        if (!folder.exists())
            folder.mkdir();

        return new File(folder + configName);
    }

    private void setDefaultConfig() {
        // Create path to desired configuration file
        File configFile = getConfigFile();

        // Attempt to create a configuration file.
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
                try (InputStream in = core.getResource("mobs/" + getName() + ".yml");
                     OutputStream out = new FileOutputStream(configFile)) {
                    ByteStreams.copy(in, out);
                }
                core.getLogger().info(getName() + " config has been loaded.");
            }
        } catch (Exception e) {
            core.getLogger().warning(getName() + " config can't be created.");
            core.getLogger().warning(e.getMessage());
        }
    }

    private boolean readConfig() {
        YamlConfiguration readConfig = YamlConfiguration.loadConfiguration(getConfigFile());

        // Check if the config has the desired version.
        int configVersion = readConfig.getInt("version", 0);
        if (configVersion != VERSION) return false;
        // Use the read config
        config = readConfig;

        // Check if the current listener should be enabled.
        return readConfig.getBoolean("enabled", false);
    }


    @Override
    public void reloadConfig() {
        boolean newState = readConfig();
        if (newState && !this.enabled) {
            this.enable();
        } else if (!newState && this.enabled) {
            this.disable();
        }

    }

    @Override
    public void initialize() {
        setDefaultConfig();
        if (readConfig()) {
            core.getLogger().info(getName() + " Loaded");
            this.reloadConfig();
            this.enable();
        }else {
            core.getLogger().info(getName() + " Not loaded");
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
