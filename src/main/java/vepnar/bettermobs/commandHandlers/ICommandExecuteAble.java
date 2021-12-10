package vepnar.bettermobs.commandHandlers;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;

public interface ICommandExecuteAble extends ICommand {
    boolean execute(Main core, CommandSender sender, String[] args);
}
