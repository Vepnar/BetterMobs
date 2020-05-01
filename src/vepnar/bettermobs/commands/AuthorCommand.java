package vepnar.bettermobs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import vepnar.bettermobs.Main;

public class AuthorCommand implements CommandExecutor {

	Main javaplugin;

	/**
	 * Initialize the Bettermobs author command listener
	 * 
	 * @param m the JavaPlugin class or main file
	 */
	public AuthorCommand(Main m) {
		javaplugin = m;
	}

	/**
	 * Handle the /bettermobs-author command. Will print information about the
	 * plugin parsed from plugin.yml
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		PluginDescriptionFile pdf = javaplugin.getDescription();
		final String messages[] = { javaplugin.prefix + " is made by §c" + pdf.getAuthors(),
				"§cSource: §fhttps://github.com/Vepnar/BetterMobs", "§cVersion: §f" + pdf.getVersion(),
				"§cWebsite: §f" + pdf.getWebsite() };
		sender.sendMessage(messages);
		return true;
	}

}
