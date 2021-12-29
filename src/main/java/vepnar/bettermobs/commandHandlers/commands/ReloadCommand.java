package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.CompletionType;
import vepnar.bettermobs.commandHandlers.GenericCommand;
import vepnar.bettermobs.commandHandlers.ICommandGroup;
import vepnar.bettermobs.genericMobs.IMobListener;

public class ReloadCommand extends GenericCommand {


    public ReloadCommand(ICommandGroup parent) {
        super("reload", parent, "§r<feature>§7 reload one or more features.", 0, CompletionType.MODULE, new String[]{"restart"});
    }


    @Override
    public String getPermission() {
        return "bettermobs.features.reload";
    }

    public void executeArguments(CommandSender sender, String[] args) {
        StringBuilder messageBuilder = new StringBuilder();

        for (String feature : args) {
            for (IMobListener listener : Main.MOB_LISTENERS) {
                if (listener.getName().equalsIgnoreCase(feature)) {
                    messageBuilder.append("§a");
                    messageBuilder.append(listener.getName());
                    messageBuilder.append("§r, ");
                    listener.reloadConfig();
                    break;
                }
            }
        }
        sender.sendMessage(Main.PREFIX + "The following features have been reloaded:");
        if(messageBuilder.length() != 0) {
            String output = messageBuilder.substring(0, messageBuilder.length() - 4);
            sender.sendMessage(output);
        } else {
            sender.sendMessage("§cNone, because your input is invalid.");
        }
    }

    @Override
    public boolean execute(Main core, CommandSender sender, String[] args) {
        if(args.length != 0) {
            executeArguments(sender, args);
            return true;
        }
        core.reloadConfig();

        for (IMobListener listener : Main.MOB_LISTENERS) {
            listener.reloadConfig();
        }
        sender.sendMessage(Main.PREFIX + "All the features have been reloaded.");
        return true;


    }
}
