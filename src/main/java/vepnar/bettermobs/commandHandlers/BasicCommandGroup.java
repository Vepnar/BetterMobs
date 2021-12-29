package vepnar.bettermobs.commandHandlers;

import java.util.ArrayList;
public class BasicCommandGroup implements ICommandGroup {

    private final ICommandGroup parent;
    private final String name;
    private final ArrayList<ICommand> subCommands = new ArrayList<>();

    public BasicCommandGroup(ICommandGroup parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public void add(ICommand cmd) {
        subCommands.add(cmd);
    }

    @Override
    public String getHelp() {
        return "";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getAlias() {
        return new String[]{"bm"};
    }

    @Override
    public int getMinimalArguments() {
        return 1;
    }

    @Override
    public CompletionType TabType() {
        return CompletionType.SUBCOMMAND;
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
    public ArrayList<ICommand> getCommands() {
        return subCommands;
    }
}
