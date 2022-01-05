package vepnar.bettermobs.runnables;

import vepnar.bettermobs.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateCheckerRunnable extends GenericRunnable {

    private static final String NAME = "UpdateChecker";
    private static UpdateCheckerRunnable instance;

    private static final int INTERVAL = 72000;
    public boolean outdated = false;
    private static final String UPDATE_PATH = "https://api.github.com/repos/Vepnar/BetterMobs/releases/latest";
    private static final Pattern VERSION_MATCHER = Pattern.compile("\\\"tag_name\"\\s*:\\s*\"([0-9]*\\.[0-9]*\\.[0-9]*)\"\\s*,");

    public static UpdateCheckerRunnable getInstance() {
        if (instance == null) {
            instance = new UpdateCheckerRunnable();
        }
        return instance;
    }

    @Override
    public void stop() {
        super.stop();
        instance = null;
    }


    public boolean start(Main main) {
        if (!super.start(main, NAME)) return false;
        try {
            task = this.runTaskTimerAsynchronously(CORE, 0, INTERVAL);
            CORE.debug(NAME + " started!");
            return true;
        } catch (IllegalStateException exception) {
            return false;
        }
    }

    @Override
    public void run() {
        if (outdated) return;
        try {

            // Connection setup.
            final URL updateUrl = new URL(UPDATE_PATH);
            final URLConnection connection = updateUrl.openConnection();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            // Retrieve information from remote host.
            StringBuilder result = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null)
                result.append(inputLine);

            // Use regular expression to match the version.
            Matcher match = VERSION_MATCHER.matcher(result.toString());
            if (match.find() && match.groupCount() > 0) {
                String latestVersion = match.group(1);
                String currentVersion = CORE.getDescription().getVersion();

                // Notify the admins that the plugin is outdated.
                if (!currentVersion.equals(latestVersion)) {
                    CORE.debug("Currently installed: " + currentVersion + " the newest version available: " + latestVersion);
                    CORE.getLogger().warning("A newer version of this plugin is out, with bugfixes & additional mobs");
                    outdated = true;
                } else CORE.debug("BetterMobs is up to date.");
            } else CORE.debug("Could not check for updates.");
            bufferedReader.close();

        } catch (Exception ex) {
            CORE.debug("Could not check for updates.\n" + ex.getMessage());
        }

    }

}
