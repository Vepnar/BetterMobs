/**
 * Main handler for the BetterMobs plugin.
 * Here all the default values and commands will be processed and/or initialized.
 *
 * @author Arjan de Haan (Vepnar)
 */
package vepnar.bettermobs;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import vepnar.bettermobs.commandHandlers.BasicCommandGroup;
import vepnar.bettermobs.commandHandlers.CommandListener;
import vepnar.bettermobs.commandHandlers.TabListener;
import vepnar.bettermobs.commandHandlers.commands.*;
import vepnar.bettermobs.genericMobs.IMobListener;
import vepnar.bettermobs.runnables.IntervalEventRunnable;
import vepnar.bettermobs.runnables.UpdateCheckerRunnable;
import vepnar.bettermobs.utils.EntityUtil;
import vepnar.bettermobs.utils.ItemUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main extends JavaPlugin {

    public static final String PREFIX = "§7[§cBetterMobs§7]§f ";
    public static final String LISTENER_SCOPE = "vepnar.bettermobs.listeners";
    public static final String ASCII_NAME = "bettermobs";
    public static int PLUGIN_ID = 13769;
    public static final String STATS_MODULES_ENABLED = "modules_enabled";
    public static final List<IMobListener> MOB_LISTENERS = new ArrayList<>();
    public final int CONFIG_VERSION = 1;
    public static int API_VERSION;

    private boolean debugMode = false;


    @Override
    public void onEnable() {
        reloadConfig();
        parseApiVersion();
        initializeCommands();
        initializeMetrics();

        try {
            // If for some reason we can't access the class path we should not load the plugin.
            loadMobListeners();
            initializeMobListener();
        } catch (IOException e) {
            // The plugin will disable itself when it doesn't have access to the class path.
            e.printStackTrace();
            getPluginLoader().disablePlugin(this);
        }

        getLogger().info("Has been enabled for minecraft 1." + API_VERSION + ".*");
        if (getEnabledListenerCount() == 0) {
            getLogger().info("There are currently no modules enabled, enable some in the config.");
        }
    }

    @Override
    public void onDisable() {
        for (IMobListener listener : MOB_LISTENERS) {
            listener.disable();
        }
        IntervalEventRunnable.getInstance().stop();
        UpdateCheckerRunnable.getInstance().stop();

        this.getLogger().info("Has been disabled.");

    }

    /**
     * Retrieve all installed mob listeners and add them to a linked list.
     * @throws IOException if the attempt to read class path resources (jar files or directories) failed.
     */
    private void loadMobListeners() throws IOException {
        // Generate constructor arguments
        Object[] args = new Object[]{this};

        Reflections reflections = new Reflections(LISTENER_SCOPE);

        // See: https://github.com/ronmamo/reflections/issues/245
        Set<Class<?>> modules = reflections.getSubTypesOf(IMobListener.class).stream().filter(v -> v.getPackage().getName().equals(LISTENER_SCOPE)).collect(Collectors.toSet());

        for (Class<?> cls : modules) {
            try {
                Class<IMobListener> module = (Class<IMobListener>) cls;
                IMobListener mobListener = module.getDeclaredConstructor(Main.class).newInstance(args);

                // Check API compatibility.
                if (mobListener.isCompatible()) {
                    MOB_LISTENERS.add(mobListener);
                } else
                    debug(mobListener.getName() + " is not compatible with the current version (1." + API_VERSION + ".*)");
            } catch (Exception ex) {
                ex.printStackTrace();
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

        // Create a new schedule one tick later.
        getServer().getScheduler().runTaskLaterAsynchronously(this, () -> {
            IntervalEventRunnable.getInstance().start(this, interval);
        }, 3);


        // Initialize the update checker.
        UpdateCheckerRunnable.getInstance().stop();

        if (getConfig().getBoolean("checkUpdates", true)) {
            // Start the update checker on a later moment.
            getServer().getScheduler().runTaskLaterAsynchronously(this, () -> {
                UpdateCheckerRunnable.getInstance().start(this);
            }, 3);

        }

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

    /**
     * Parse the current API version.
     */
    private void parseApiVersion() {
        String versionString = getServer().getBukkitVersion();
        for (int i = 13; 19 > i; i++) {
            if (versionString.contains("1." + i)) {
                API_VERSION = i;
            }
        }
    }


    private void initializeMetrics() {
        Metrics metrics = new Metrics(this, PLUGIN_ID);
        metrics.addCustomChart(new SimplePie(STATS_MODULES_ENABLED, () -> String.valueOf(getEnabledListenerCount())));
    }
}
