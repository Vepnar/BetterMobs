package vepnar.bettermobs.commandHandlers;

public interface Command {
    String getHelp();
    String getName();
    String[] getAlias();
    int getMinimalArguments();
    CompletionType TabType();
    String getPermission();

    CommandGroup getParent();

}
