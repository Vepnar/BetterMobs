package vepnar.bettermobs.commandHandlers;

import java.util.ArrayList;

public interface ICommandGroup extends ICommand {
    ArrayList<ICommand>  getCommands();
}
