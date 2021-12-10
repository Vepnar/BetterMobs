package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.ICommandExecuteAble;
import vepnar.bettermobs.commandHandlers.ICommandGroup;
import vepnar.bettermobs.genericMobs.IMobListener;

import java.util.List;
import java.util.Locale;

public class EnableCommand implements ICommandExecuteAble {

    private final ICommandGroup parent;

    public EnableCommand(ICommandGroup parent){
        this.parent = parent;
    }

    @Override
    public String getHelp() {
        return "§r<feature>§7 Enable a feature.";
    }

    @Override
    public String getName() {
        return "enable";
    }

    @Override
    public String[] getAlias() {
        return new String [] {"e", "start"};
    }

    @Override
    public int getMinimalArguments() {
        return 1;
    }

    @Override
    public String[] getTabCompletion() {
        return new String[0];
    }

    @Override
    public String getPermission() {
        return "bettermobs.features.enable";
    }

    @Override
    public ICommandGroup getParent() {
        return parent;
    }

    @Override
    public boolean execute(Main core, CommandSender sender, String[] args) {
        String output = "";

        for(String feature : args) {
            for(IMobListener listener :  core.mobListeners) {
                if (listener.getName().equalsIgnoreCase(feature) && !listener.isEnabled()) {
                    output += "§a" + listener.getName() + "§r, ";
                    listener.enable();
                    break;
                }
            }
        }
        sender.sendMessage(core.prefix + "The following features have been enabled:");
        if(output.length() != 0) {
            output = output.substring(0, output.length() - 4);
            sender.sendMessage(output);
        } else {
            sender.sendMessage("§cNone, because your input is invalid.");
        }
        return true;
    }
}
