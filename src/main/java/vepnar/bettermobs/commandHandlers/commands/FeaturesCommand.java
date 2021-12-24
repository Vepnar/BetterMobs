package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.CompletionType;
import vepnar.bettermobs.commandHandlers.ICommandExecuteAble;
import vepnar.bettermobs.commandHandlers.ICommandGroup;
import vepnar.bettermobs.genericMobs.IMobListener;

import java.util.List;

/**
 * Handle the /bettermobs author. Will print information about the
 * plugin parsed from plugin.yml
 */
public class FeaturesCommand implements ICommandExecuteAble {

    private final ICommandGroup parent;

    public FeaturesCommand(ICommandGroup parent){
        this.parent = parent;
    }

    @Override
    public String getHelp() {
        return "Shows all installed features";
    }

    @Override
    public String getName() {
        return "features";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"feature", "list"};
    }

    @Override
    public int getMinimalArguments() {
        return 0;
    }

    @Override
    public CompletionType TabType() {
        return CompletionType.NOTHING;
    }

    @Override
    public String getPermission() {
        return "bettermobs.features.show";
    }

    @Override
    public ICommandGroup getParent() {
        return parent;
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
