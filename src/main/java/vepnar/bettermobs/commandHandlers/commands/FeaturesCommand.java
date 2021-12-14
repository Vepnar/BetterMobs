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
        return new String [] {"feature", "f", "list", "l"};
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
        List<IMobListener> listeners = core.mobListeners;
        sender.sendMessage(core.prefix + "(" +listeners.size()+ ") Features installed:");
        String output = "";
        for(IMobListener listener : listeners) {
            String color = listener.isEnabled() ? "§a" : "§c";
            output += color + listener.getName() + "§r, ";
        }
        if(output.length() != 0) {
            output = output.substring(0, output.length() - 4);
            sender.sendMessage(output);
        }
        return true;
    }
}
