package vepnar.bettermobs.commandHandlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.IMobListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The current implementation doesn't support multi-argument commands.
 */
public class TabListener implements TabCompleter {

    Main core;
    BasicCommandGroup bettermobs;

    public TabListener(Main main, BasicCommandGroup bettermobs) {
        this.core = main;
        this.bettermobs = bettermobs;
    }

    private List<String> getSubCommands(CommandSender sender, ICommandGroup command) {
        ArrayList<String> tabs = new ArrayList<>();
        for (ICommand subCommand : command.getCommands()) {

            // Only show subcommands the sender has access to.
            if (!CommandUtils.hasPermissions(sender, subCommand)) continue;

            tabs.add(subCommand.getName());
        }
        return tabs;
    }

    private List<String> getModules() {
        ArrayList<String> names = new ArrayList<>();
        for (IMobListener listener : core.mobListeners) {
            names.add(listener.getName());
        }
        return names;
    }

    private List<String> getCompletionList(CommandSender sender, ICommand command) {
        switch (command.TabType()) {
            case SUBCOMMAND:
                if (command instanceof ICommandGroup) {
                    return getSubCommands(sender, (ICommandGroup) command);
                } else {
                    core.getLogger().warning("Error trying to expand tab of " + command.getName());
                    return null;
                }
            case MODULE:
                return getModules();
            case NOTHING:
                return new ArrayList<>();
            default:
                return null;

        }
    }

    private ICommand getSubCommand(ICommand command, String targetString) {
        // Only traverse command groups.
        if (!(command instanceof ICommandGroup)) return null;
        ICommandGroup group = (ICommandGroup) command;

        // Return a subcommand if found.
        for (ICommand child : group.getCommands()) {
            if (child.getName().equalsIgnoreCase(targetString) || Arrays.asList(child.getAlias()).contains(targetString))
                return child;
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ICommand deepestCommand = bettermobs;
        for (String target : args) {
            ICommand matching = getSubCommand(deepestCommand, target);
            if (matching != null) {
                if (CommandUtils.hasPermissions(sender, matching)) {
                    deepestCommand = matching;
                } else return null;
            } else break;
        }
        return getCompletionList(sender, deepestCommand);
    }
}
