package vepnar.bettermobs.commandHandlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.IMobListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        for (IMobListener listener : Main.MOB_LISTENERS) {
            names.add(listener.getName());
        }
        return names;
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

    private List<String> getSuggestionList(CommandSender sender, ICommand command) {
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

    private List<String> improveSuggestions(List<String> suggestions, String input) {
        if (suggestions == null || suggestions.isEmpty()) return null;
        return suggestions.stream().filter(suggestion -> suggestion.toLowerCase().startsWith(input)).collect(Collectors.toList());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ICommand deepestCommand = bettermobs;
        int deepestIndex = 0;
        for (String arg : args) {
            ICommand matching = getSubCommand(deepestCommand, arg);
            if (matching != null) {
                if (CommandUtils.hasPermissions(sender, matching)) {
                    deepestCommand = matching;
                    deepestIndex += 1;
                } else return null;
            } else break;
        }
        List<String> suggestions = getSuggestionList(sender, deepestCommand);
        if (deepestIndex == args.length - 1) {
            suggestions = improveSuggestions(suggestions, args[deepestIndex]);
        }


        return suggestions;
    }
}
