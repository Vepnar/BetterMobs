package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.CompletionType;
import vepnar.bettermobs.commandHandlers.GenericCommand;
import vepnar.bettermobs.commandHandlers.ICommandGroup;
import vepnar.bettermobs.genericMobs.IMobListener;

import java.util.List;

/**
 * Handle the /bettermobs author. Will print information about the
 * plugin parsed from plugin.yml
 */
public class FeaturesCommand extends GenericCommand {

    public FeaturesCommand(ICommandGroup parent) {
        super("features", parent, "Shows all installed features", 0, CompletionType.NOTHING, new String[]{"feature", "list", "modules", "module"});
    }

    @Override
    public String getPermission() {
        return "bettermobs.features.show";
    }


    @Override
    public boolean execute(Main core, CommandSender sender, String[] args) {
        List<IMobListener> listeners = Main.MOB_LISTENERS;
        sender.sendMessage(Main.PREFIX + "(" + listeners.size() + ") Features installed:");
        StringBuilder messageBuilder = new StringBuilder();
        for (IMobListener listener : listeners) {
            messageBuilder.append(listener.isEnabled() ? "§a" : "§c");
            messageBuilder.append(listener.getName());
            messageBuilder.append("§r, ");
        }
        if (messageBuilder.length() != 0) {
            String message = messageBuilder.toString();
            message = message.substring(0, message.length() - 4);
            sender.sendMessage(message);
        }
        return true;
    }
}
