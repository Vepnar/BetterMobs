/**
 * Main handler for the BetterMobs plugin.
 * Here all the default values and commands will be processed and/or initialized.
 *
 * @author Arjan de Haan (Vepnar)
 */
package vepnar.bettermobs;

import com.google.common.reflect.ClassPath;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;
import vepnar.bettermobs.commandHandlers.BasicCommandGroup;
import vepnar.bettermobs.commandHandlers.CommandListener;
import vepnar.bettermobs.commandHandlers.TabListener;
import vepnar.bettermobs.commandHandlers.commands.*;
import vepnar.bettermobs.genericMobs.IMobListener;
import vepnar.bettermobs.utils.EntityUtil;
import vepnar.bettermobs.utils.ItemUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    public static final String PREFIX = "§7[§cBetterMobs§7]§f ";
    public static int pluginId = 13769;
    public static final String ASCII_NAME = "bettermobs";
    private final int CONFIG_VERSION = 1;
    public static final String STATS_MODULES_ENABLED = "modules_enabled";
    public static final List<IMobListener> MOB_LISTENERS = new ArrayList<>();
    private boolean debugMode = false;


    @Override
    public void onEnable() {

        // Load configuration
        loadDefaultConfig();
        reloadConfig();
        initializeCommands();

        try {
            // If for some reason we can't access the class path we should not load the plugin.
            loadMobListeners();
            initializeMobListener();
        } catch (IOException e) {
            e.printStackTrace();

            // The plugin will disable itself when it doesn't have access to the class path.
            getPluginLoader().disablePlugin(this);
        }

        initializeMetrics();
        getLogger().info("Has been enabled.");
        if (getEnabledListenerCount() == 0) {
            getLogger().info("There are currently no modules enabled, enable some in the config.");
        }
    }

    @Override
    public void onDisable() {
        for (IMobListener listener : MOB_LISTENERS) {
            listener.disable();
        }
        this.getLogger().info("Has been disabled.");
        IntervalEventRunnable.getInstance().stop();
    }

    /**
     * Retrieve all installed mob listeners and add them to a linked list.
     * @throws IOException if the attempt to read class path resources (jar files or directories) failed.
     */
    private void loadMobListeners() throws IOException {
        ClassPath classpath = ClassPath.from(this.getClassLoader());

        // Generate constructor arguments
        Object[] args = new Object[]{this};

        // Scan for other classes.
        for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClasses("vepnar.bettermobs.listeners")) {
            try {
                Class<?> cls = classInfo.load();

                // Check if the loaded class implements the interface IMobListener.
                // If it does implement this interface, add it to the linked list.
                if (IMobListener.class.isAssignableFrom(cls)) {
                    Class<IMobListener> mobListenerClass = (Class<IMobListener>) cls;
                    IMobListener mobListener = mobListenerClass.getDeclaredConstructor(Main.class).newInstance(args);
                    MOB_LISTENERS.add(mobListener);
                }
            } catch (Exception ex) {
                    getLogger().warning(ex.getMessage());
                }
            }
    }

    private void initializeMobListener() {
        for (IMobListener listener : MOB_LISTENERS) {
            // Initialize will also enable the listener if possible.
            listener.initialize();
        }
    }

    /**
     * Attempt to create a configuration file in BetterMobs directory when there is
     * no configuration file.
     */
    public void loadDefaultConfig() {
        File config = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            this.saveResource("config.yml", false);
        }
    }

    @Override
    public void reloadConfig() {
        this.loadDefaultConfig();
        super.reloadConfig();

        int currentVersion = getConfig().getInt("version", 0);
        if (currentVersion != CONFIG_VERSION) {
            getLogger().warning("Version number in `config.yml` doesn't match with the required config version.");
            getLogger().warning("Plugin could behave in an unpredictable manner, please update the config.");
        }

        this.debugMode = getConfig().getBoolean("debug", false);

        // Change interval settings:
        long interval = getConfig().getLong("updateInterval", 20);
        // These have to be separate since the stop method destroys itself.
        IntervalEventRunnable.getInstance().stop();
        IntervalEventRunnable.getInstance().start(this, interval);

        // Update utils
        ItemUtil.reloadAll(this);
        EntityUtil.reloadAll(this);

    }

    public void debug(String log) {
        if (debugMode) getLogger().info(log);
    }

    /**
     * Register command handlers.
     */
    public void initializeCommands() {

        BasicCommandGroup betterMobs = new BasicCommandGroup(null, ASCII_NAME);
        betterMobs.add(new HelpCommand(betterMobs));
        betterMobs.add(new AuthorCommand(betterMobs));
        betterMobs.add(new FeaturesCommand(betterMobs));
        betterMobs.add(new EnableCommand(betterMobs));
        betterMobs.add(new DisableCommand(betterMobs));
        betterMobs.add(new ReloadCommand(betterMobs));

        // Register command
        getCommand(ASCII_NAME).setExecutor(new CommandListener(this, betterMobs));

        // Register tab completer
        getCommand(ASCII_NAME).setTabCompleter(new TabListener(this, betterMobs));
        debug("All commands initialized");
    }

    private int getEnabledListenerCount() {
        int count = 0;
        for (IMobListener listener : MOB_LISTENERS) {
            if (listener.isEnabled()) count++;
        }
        return count;
    }

    private void initializeMetrics() {
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SimplePie(STATS_MODULES_ENABLED, () -> String.valueOf(getEnabledListenerCount())));

    }
}
