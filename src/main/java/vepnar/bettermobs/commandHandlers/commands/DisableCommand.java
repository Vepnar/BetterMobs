package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.CompletionType;
import vepnar.bettermobs.commandHandlers.GenericCommand;
import vepnar.bettermobs.commandHandlers.CommandGroup;
import vepnar.bettermobs.genericMobs.MobListener;

public class DisableCommand extends GenericCommand {


    public DisableCommand(CommandGroup parent) {
        super("disable", parent, "§r<feature>§7 Disable a feature.", 1, CompletionType.MODULE, new String[]{"stop"});
    }

    @Override
    public String getPermission() {
        return "bettermobs.features.disable";
    }

    @Override
    public boolean execute(Main core, CommandSender sender, String[] args) {

        StringBuilder messageBuilder = new StringBuilder();
        for(String feature : args) {
            for (MobListener listener : Main.MOB_LISTENERS) {
                if (listener.getName().equalsIgnoreCase(feature) && listener.isEnabled()) {
                    listener.disable();
                    messageBuilder.append("§c");
                    messageBuilder.append(listener.getName());
                    messageBuilder.append("§r, ");
                    break;
                }
            }
        }
        sender.sendMessage(Main.FANCY_NAME + "The following features have been disabled:");
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
