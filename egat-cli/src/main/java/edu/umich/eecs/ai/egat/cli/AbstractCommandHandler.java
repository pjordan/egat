package edu.umich.eecs.ai.egat.cli;

import org.apache.commons.cli2.option.Command;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.CommandBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.CommandLine;

/**
 * @author Patrick Jordan
 */
public abstract class AbstractCommandHandler implements CommandHandler {
    protected Command command;

    protected AbstractCommandHandler() {
        command = createCommand();
    }

    public Command getCommand() {
        return command;
    }

    protected void addCommandDescription(CommandBuilder commandBuilder) {
    }

    protected void addChildOptions(GroupBuilder groupBuilder) {
    }

    protected Command createCommand() {
        final CommandBuilder commandBuilder = new CommandBuilder();

        final GroupBuilder groupBuilder = new GroupBuilder();

        commandBuilder.withName(getCommandName());

        addCommandDescription(commandBuilder);

        addChildOptions(groupBuilder);

        
        return commandBuilder.withChildren(groupBuilder.create()).create();
    }

    public abstract void handleCommand(CommandLine commandLine) throws CommandProcessingException;

    protected abstract String getCommandName();
}
