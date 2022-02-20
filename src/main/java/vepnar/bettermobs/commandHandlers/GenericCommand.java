package vepnar.bettermobs.commandHandlers;

import org.bukkit.command.CommandSender;
import vepnar.bettermobs.Main;

public class GenericCommand implements CommandExecuteAble {

    private final String NAME;
    private final int MINIMAL_ARGUMENTS;
    private final CompletionType COMPLETION_TYPE;
    private final String[] ALIASES;
    private final CommandGroup PARENT;
    private final String HELP;

    public GenericCommand(String name, CommandGroup parent, String help, int minimal_arguments, CompletionType completion_type, String[] aliases) {
        NAME = name;
        MINIMAL_ARGUMENTS = minimal_arguments;
        COMPLETION_TYPE = completion_type;
        ALIASES = aliases;
        PARENT = parent;
        HELP = help;
    }


    @Override
    public String getHelp() {
        return HELP;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getAlias() {
        return ALIASES;
    }

    @Override
    public int getMinimalArguments() {
        return MINIMAL_ARGUMENTS;
    }

    @Override
    public CompletionType TabType() {
        return COMPLETION_TYPE;
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public CommandGroup getParent() {
        return PARENT;
    }

    @Override
    public boolean execute(Main core, CommandSender sender, String[] args) {
        return false;
    }
}
