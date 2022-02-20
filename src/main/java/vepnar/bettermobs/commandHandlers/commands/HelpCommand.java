package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.*;

public class HelpCommand extends GenericCommand {

    public HelpCommand(CommandGroup parent) {
        super("help", parent, "Display this information", 0, CompletionType.NOTHING, new String[0]);

    }

    @Override
    public boolean execute(Main core, CommandSender sender, String[] args) {
        sender.sendMessage(Main.FANCY_NAME + "Displaying help of: §c" + getParent().getName());
        for (Command command : getParent().getCommands()) {
            if (CommandUtils.hasPermissions(sender, command)) {
                sender.sendMessage(CommandUtils.getHelpCommand(command));
            }
        }
        return true;
    }
}
