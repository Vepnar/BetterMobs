package vepnar.bettermobs.commandHandlers;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;

public interface CommandExecuteAble extends Command {
    boolean execute(Main core, CommandSender sender, String[] args);
}
