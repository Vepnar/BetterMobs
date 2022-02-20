package vepnar.bettermobs.commandHandlers;

import java.util.ArrayList;

public interface CommandGroup extends Command {
    ArrayList<Command>  getCommands();
}
