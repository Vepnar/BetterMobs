package vepnar.bettermobs.commandHandlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import vepnar.bettermobs.Main;

import java.util.List;

public class TabListener implements TabCompleter {

    Main core;
    BasicCommandGroup bettermobs;

    public TabListener(Main main, BasicCommandGroup bettermobs) {
        this.core = main;
        this.bettermobs = bettermobs;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
