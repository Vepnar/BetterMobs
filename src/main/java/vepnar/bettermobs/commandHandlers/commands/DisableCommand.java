package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.CompletionType;
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
        return new String[]{"d", "stop"};
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
        return "bettermobs.features.disable";
    }

    @Override
    public ICommandGroup getParent() {
        return parent;
    }

    @Override
    public boolean execute(Main core, CommandSender sender, String[] args) {

        StringBuilder messageBuilder = new StringBuilder();
        for(String feature : args) {
            for(IMobListener listener : core.mobListeners) {
                if (listener.getName().equalsIgnoreCase(feature) && listener.isEnabled()) {
                    listener.disable();
                    messageBuilder.append("§c");
                    messageBuilder.append(listener.getName());
                    messageBuilder.append("§r, ");
                    break;
                }
            }
        }
        sender.sendMessage(core.prefix + "The following features have been disabled:");
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
