package vepnar.bettermobs.commandHandlers;

import org.bukkit.command.CommandSender;

public class CommandUtils {
    public static String getHelpCommand(ICommand command) {
        String output = "§r§7" + command.getHelp();
        ICommand parent = command;
        while (parent != null) {
            output = parent.getName() + " " + output;
            parent = parent.getParent();
        }

        output = "/" + output;
        return output;
    }

    public static boolean hasPermissions(CommandSender sender, ICommand command) {
        return command.getPermission() == null || sender.hasPermission(command.getPermission());
    }
}
