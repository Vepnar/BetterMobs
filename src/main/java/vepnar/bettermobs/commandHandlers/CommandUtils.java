package vepnar.bettermobs.commandHandlers;

import org.bukkit.command.CommandSender;

public class CommandUtils {
    public static String getHelpCommand(Command command) {
        StringBuilder messageBuilder = new StringBuilder("§r§7" + command.getHelp());
        Command parent = command;
        while (parent != null) {
            messageBuilder.insert(0, parent.getName() + " ");
            parent = parent.getParent();
        }
        messageBuilder.insert(0, '/');
        return messageBuilder.toString();
    }

    public static boolean hasPermissions(CommandSender sender, Command command) {
        return command.getPermission() == null || sender.hasPermission(command.getPermission());
    }
}
