package vepnar.bettermobs.commandHandlers;

public interface ICommand {
    String getHelp();
    String getName();
    String[] getAlias();
    int getMinimalArguments();
    String[] getTabCompletion();
    String getPermission();
    ICommand getParent();

}
