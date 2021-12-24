package vepnar.bettermobs.commandHandlers.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.commandHandlers.CompletionType;
import vepnar.bettermobs.commandHandlers.ICommandExecuteAble;
import vepnar.bettermobs.commandHandlers.ICommandGroup;

/**
 * Handle the /bettermobs author. Will print information about the
 * plugin parsed from plugin.yml
 */
public class AuthorCommand implements ICommandExecuteAble {

    private final ICommandGroup parent;

    public AuthorCommand(ICommandGroup parent){
        this.parent = parent;
    }

    @Override
    public String getHelp() {
        return "Credits the developer";
    }

    @Override
    public String getName() {
        return "author";
    }

    @Override
    public String[] getAlias() {
        return new String [] {"credit", "credits"};
    }

    @Override
    public int getMinimalArguments() {
        return 0;
    }

    @Override
    public CompletionType TabType() {
        return CompletionType.NOTHING;
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public ICommandGroup getParent() {
        return parent;
    }

    @Override
    public boolean execute(Main core, CommandSender sender, String[] args) {
        PluginDescriptionFile pdf = core.getDescription();
        sender.sendMessage(Main.PREFIX + "Is made by §c" + pdf.getAuthors());
        sender.sendMessage("§cSource: §fhttps://github.com/Vepnar/BetterMobs");
        sender.sendMessage("§cVersion: §f" + pdf.getVersion());
        if (pdf.getWebsite() != null) {
            sender.sendMessage("§cWebsite: §f" + pdf.getWebsite());
        }
        return true;
    }
}
