package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.CommandUtils;
import vepnar.bettermobs.commandHandlers.ICommand;
import vepnar.bettermobs.commandHandlers.ICommandExecuteAble;
import vepnar.bettermobs.commandHandlers.ICommandGroup;

public class HelpCommand implements ICommandExecuteAble {

    private final ICommandGroup parent;

    public HelpCommand(ICommandGroup parent){
        this.parent = parent;
    }

    @Override
    public String getHelp() {
        return "Display this information";
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public int getMinimalArguments() {
        return 0;
    }

    @Override
    public String[] getTabCompletion() {
        return new String[0];
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public ICommand getParent() {
        return parent;
    }

    @Override
    public boolean execute(Main core, CommandSender sender, String[] args) {
        sender.sendMessage(core.prefix + "Displaying help of: §c" + parent.getName());
        for (ICommand command : parent.getCommands()) {
            if(CommandUtils.hasPermissions(sender, command)) {
                sender.sendMessage(CommandUtils.getHelpCommand(command));
            }
        }
        return true;
    }
}
