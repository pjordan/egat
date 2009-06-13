/*
 * AbstractCommandHandler.java
 *
 * Copyright (C) 2006-2009 Patrick R. Jordan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
