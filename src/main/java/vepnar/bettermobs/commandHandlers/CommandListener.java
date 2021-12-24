package vepnar.bettermobs.commandHandlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandListener implements CommandExecutor {
    Main core;
    BasicCommandGroup bettermobs;
    String[] HELP = new String[]{"help"};

    public CommandListener(Main m, BasicCommandGroup bettermobs) {
        this.bettermobs = bettermobs;
        this.core = m;
    }

    public boolean executeCommand(ArrayList<ICommand> environment, CommandSender sender, String[] args) {
        if (args.length == 0) return false;
        String targetSubCommand = args[0].toLowerCase();
        String[] poppedArgs = Arrays.copyOfRange(args, 1, args.length);
        for (ICommand command : environment) {
            // Only process commands that match
            if (!(command.getName().equals(targetSubCommand) || Arrays.stream(command.getAlias()).anyMatch(c -> c.equals(targetSubCommand)))) {
                continue;
            }

            // Verify it the user has enough permissions.
            if (!CommandUtils.hasPermissions(sender, command)) {
                sender.sendMessage(Main.PREFIX + "§cNot enough permissions.");
                return true;
            }

            // Verify if the command has enough arguments.
            if (command.getMinimalArguments() >= targetSubCommand.length()) {
                sender.sendMessage(Main.PREFIX + CommandUtils.getHelpCommand(command));
                return true;
            }

            if (command instanceof ICommandGroup) {

                ICommandGroup commandGroup = (ICommandGroup) command;

                boolean result = executeCommand(commandGroup.getCommands(), sender, poppedArgs);
                if(!result) {
                    // Request the help command.
                    return executeCommand(commandGroup.getCommands(), sender, HELP);
                } else {
                    return true;
                }
            } else if (command instanceof ICommandExecuteAble) {
                ICommandExecuteAble executeAble = (ICommandExecuteAble) command;
                return executeAble.execute(core, sender, poppedArgs);
            }
        }
        // Prevent infinite recursion.
        if(targetSubCommand.equals("help")) return false;

        // Restart but request the help command.
        return executeCommand(environment, sender, HELP);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Show the help command when there are no commands entered.
        if(args.length == 0 ) args = HELP;
        return executeCommand(bettermobs.getCommands(), sender, args);
    }
}
