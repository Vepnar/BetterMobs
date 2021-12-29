package vepnar.bettermobs.commandHandlers;

public interface ICommand {
    String getHelp();
    String getName();
    String[] getAlias();
    int getMinimalArguments();
    CompletionType TabType();
    String getPermission();

    ICommandGroup getParent();

}
