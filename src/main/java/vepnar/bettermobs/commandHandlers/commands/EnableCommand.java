package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.CompletionType;
import vepnar.bettermobs.commandHandlers.GenericCommand;
import vepnar.bettermobs.commandHandlers.CommandGroup;
import vepnar.bettermobs.genericMobs.GenericMob;

public class EnableCommand extends GenericCommand {


    public EnableCommand(CommandGroup parent) {
        super("enable", parent, "§r<feature>§7 Enable a feature.", 1, CompletionType.MODULE, new String[]{"start"});
    }

    @Override
    public String getPermission() {
        return "bettermobs.features.enable";
    }
    @Override
    public boolean execute(Main core, CommandSender sender, String[] args) {
        StringBuilder messageBuilder = new StringBuilder();
        for(String feature : args) {
            for (GenericMob module : Main.MOB_LISTENERS) {
                if (module.getName().equalsIgnoreCase(feature) && !module.isEnabled()) {
                    module.enable();
                    messageBuilder.append("§a");
                    messageBuilder.append(module.getName());
                    messageBuilder.append("§r, ");
                    break;
                }
            }
        }
        sender.sendMessage(Main.FANCY_NAME + "The following features have been enabled:");
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
