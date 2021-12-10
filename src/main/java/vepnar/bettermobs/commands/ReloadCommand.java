package vepnar.bettermobs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import vepnar.bettermobs.Main;

public class ReloadCommand implements CommandExecutor {

	Main javaplugin;

	/**
	 * Initialize the Bettermobs reload command listener
	 * 
	 * @param m the JavaPlugin class or main file
	 */
	public ReloadCommand(Main m) {
		javaplugin = m;
	}

	/**
	 * Handle the /bettermobs-reload and /bmr command. Reload config, create new
	 * config and reload events. Will also send a message when the reload has
	 * completed.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		javaplugin.reloadConfig();
		javaplugin.loadDefaultConfig();
		javaplugin.enableEvents();
		sender.sendMessage(javaplugin.prefix + "has been reloaded");
		return true;
	}
}
