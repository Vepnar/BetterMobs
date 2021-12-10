package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.ICommandExecuteAble;
import vepnar.bettermobs.commandHandlers.ICommandGroup;
import vepnar.bettermobs.genericMobs.IMobListener;

public class DisableCommand implements ICommandExecuteAble {

    private final ICommandGroup parent;

    public DisableCommand(ICommandGroup parent){
        this.parent = parent;
    }

    @Override
    public String getHelp() {
        return "§r<feature>§7 Disable a feature.";
    }

    @Override
    public String getName() {
        return "disable";
    }

    @Override
    public String[] getAlias() {
        return new String [] {"d", "stop"};
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
        return "bettermobs.features.disable";
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
                if (listener.getName().equalsIgnoreCase(feature) && listener.isEnabled()) {
                    output += "§a" + listener.getName() + "§r, ";
                    listener.disable();
                    break;
                }
            }
        }
        sender.sendMessage(core.prefix + "The following features have been disabled:");
        if(output.length() != 0) {
            output = output.substring(0, output.length() - 4);
            sender.sendMessage(output);
        } else {
            sender.sendMessage("§cNone, because your input is invalid.");
        }
        return true;
    }
}
