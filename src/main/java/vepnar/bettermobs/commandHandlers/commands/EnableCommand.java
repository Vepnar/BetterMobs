package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.CompletionType;
import vepnar.bettermobs.commandHandlers.ICommandExecuteAble;
import vepnar.bettermobs.commandHandlers.ICommandGroup;
import vepnar.bettermobs.genericMobs.IMobListener;

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
        return new String[]{"e", "start"};
    }

    @Override
    public int getMinimalArguments() {
        return 1;
    }

    @Override
    public CompletionType TabType() {
        return CompletionType.MODULE;
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
        StringBuilder messageBuilder = new StringBuilder();
        for(String feature : args) {
            for(IMobListener listener :  core.mobListeners) {
                if (listener.getName().equalsIgnoreCase(feature) && !listener.isEnabled()) {
                    messageBuilder.append("§a");
                    messageBuilder.append(listener.getName());
                    messageBuilder.append("§r, ");
                    break;
                }
            }
        }
        sender.sendMessage(core.prefix + "The following features have been enabled:");
        if(messageBuilder.length() != 0) {
            String message = messageBuilder.toString();
            message = message.substring(0, message.length() - 4);
            sender.sendMessage(message);
        } else {
            sender.sendMessage("§cNone, because your input is invalid.");
        }
        return true;
    }
}
