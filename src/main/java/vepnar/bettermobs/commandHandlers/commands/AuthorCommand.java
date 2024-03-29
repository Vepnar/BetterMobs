package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.CompletionType;
import vepnar.bettermobs.commandHandlers.GenericCommand;
import vepnar.bettermobs.commandHandlers.CommandGroup;
import vepnar.bettermobs.updateChecker.UpdateCheckerRunnable;

/**
 * Handle the /bettermobs author. Will print information about the
 * plugin parsed from plugin.yml
 */
public class AuthorCommand extends GenericCommand {


    public AuthorCommand(CommandGroup parent) {
        super("author", parent, "Credits the developer", 0, CompletionType.NOTHING, new String[]{"credit", "credits", "version"});
    }

    @Override
    public boolean execute(Main core, CommandSender sender, String[] args) {
        PluginDescriptionFile pdf = core.getDescription();
        sender.sendMessage(Main.FANCY_NAME + "has been developed by §c" + pdf.getAuthors());
        sender.sendMessage("§cSource: §fhttps://github.com/Vepnar/BetterMobs");
        sender.sendMessage("§cVersion: §f" + pdf.getVersion());
        sender.sendMessage("§cUpdate status: §f" + UpdateCheckerRunnable.getState().getDescription());
        if (pdf.getWebsite() != null) {
            sender.sendMessage("§cWebsite: §f" + pdf.getWebsite());
        }
        return true;
    }
}
