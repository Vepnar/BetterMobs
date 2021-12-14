/**
 * Main handler for the BetterMobs plugin.
 * Here all the default values and commands will be processed and/or initialized.
 *
 * @author Arjan de Haan (Vepnar)
 */
package vepnar.bettermobs;

import com.google.common.io.ByteStreams;
import com.google.common.reflect.ClassPath;
import org.bukkit.plugin.java.JavaPlugin;
import vepnar.bettermobs.commandHandlers.BasicCommandGroup;
import vepnar.bettermobs.commandHandlers.CommandListener;
import vepnar.bettermobs.commandHandlers.TabListener;
import vepnar.bettermobs.commandHandlers.commands.*;
import vepnar.bettermobs.genericMobs.IMobListener;
import vepnar.bettermobs.genericMobs.MobListenerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    public String prefix = "§7[§cBetterMobs§7]§f ";
	public List<IMobListener> mobListeners = new ArrayList<>();


    @Override
    public void onEnable() {

        // Load configuration
        loadDefaultConfig();
        initializeCommands();

        try {
            // If for some reason we can't access the class path we should not load the plugin.
            loadMobListeners();
            initializeMobListener();
        } catch (IOException e) {
            e.printStackTrace();

            // The plugin will disable itself when it doesn't have access to the class path.
            this.getPluginLoader().disablePlugin(this);
        }
        this.getLogger().info("Has been enabled.");
    }

    @Override
    public void onDisable() {
        for (IMobListener listener : mobListeners) {
            listener.disable();
        }
        this.getLogger().info("Has been disabled.");
    }

    /**
     * Retrieve all installed mob listeners and add them to a linked list.
     * @throws IOException if the attempt to read class path resources (jar files or directories) failed.
     */
    private void loadMobListeners() throws IOException {
        ClassPath classpath = ClassPath.from(this.getClassLoader());
        // Scan for other classes.
        for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClasses("vepnar.bettermobs.listeners")) {
            try {
                Class<?> cls = classInfo.load();

                // Check if the loaded class implements the interface IMobListener.
                // If it does implement this interface, add it to the linked list.
                if (IMobListener.class.isAssignableFrom(cls)) {
                    Class<IMobListener> mobListenerClass = (Class<IMobListener>) cls;
                    IMobListener listener = MobListenerFactory.createMobListener(this, mobListenerClass);

                    mobListeners.add(listener);
                }
            } catch (Exception ex) {
                    getLogger().warning(ex.getMessage());
                }
            }
    }

    private void initializeMobListener() {
        for (IMobListener listener : mobListeners) {
            // Initialize will also enable the listener if possible.
            listener.initialize();
        }
    }

    /**
     * Attempt to create a configuration file in BetterMobs directory when there is
     * no configuration file.
     */
    public void loadDefaultConfig() {

        // Create the BetterMobs directory when it does not exist.
        File folder = getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, "config.yml");

        // Attempt to write to the configuration file.
        try {
            if (!resourceFile.exists()) {
                if (!resourceFile.createNewFile()) {
                    throw new IOException("Couldn't create " + resourceFile.getAbsolutePath());
                }
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

        BasicCommandGroup bettermobs = new BasicCommandGroup(null, "bettermobs");
        bettermobs.add(new HelpCommand(bettermobs));
        bettermobs.add(new AuthorCommand(bettermobs));
        bettermobs.add(new FeaturesCommand(bettermobs));
        bettermobs.add(new EnableCommand(bettermobs));
        bettermobs.add(new DisableCommand(bettermobs));

        // Register command
        getCommand("bettermobs").setExecutor(new CommandListener(this, bettermobs));

        // Register tab completer
        getCommand("bettermobs").setTabCompleter(new TabListener(this, bettermobs));
    }
}
