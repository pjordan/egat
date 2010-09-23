/*
 * RemoveActionCommandHandler.java
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
package egat.cli.removeaction;

import egat.cli.AbstractGameCommandHandler;
import egat.cli.CommandProcessingException;
import egat.gamexml.SymmetricGameWriter;
import egat.gamexml.StrategicGameWriter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.CommandBuilder;

import java.util.Set;
import java.util.HashSet;
import java.io.IOException;

/**
 * @author Patrick R. Jordan
 */
public class RemoveActionCommandHandler extends AbstractGameCommandHandler {
    private Option playerOption;

    private String playerId;

    private Option actionOption;

    private String actionId;

    @Override
    protected void addAdditionalChildOptions(GroupBuilder groupBuilder) {
        final DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();
        final ArgumentBuilder argumentBuilder = new ArgumentBuilder();


        playerOption = defaultOptionBuilder.withLongName("player")
                .withArgument(argumentBuilder.withMinimum(1)
                        .withMaximum(1)
                        .withName("id").create())
                .withDescription("id of player").create();

        groupBuilder.withOption(playerOption);

        actionOption = defaultOptionBuilder.withLongName("action")
                .withArgument(argumentBuilder.withMinimum(1)
                        .withMaximum(1)
                        .withName("id").create())
                .withDescription("id of action").create();

        groupBuilder.withOption(actionOption);
    }


    @Override
    protected void handleAdditionalChildOptions(CommandLine commandLine) throws CommandProcessingException {
        if (commandLine.hasOption(actionOption)) {
            actionId = (String) commandLine.getValue(actionOption);
        }

        if (commandLine.hasOption(playerOption)) {
            playerId = (String) commandLine.getValue(playerOption);
        }
    }

    protected String getCommandName() {
        return "remove-action";
    }

    protected void processSymmetricGame(MutableSymmetricGame game) throws CommandProcessingException {
        Set<Action> actions = new HashSet<Action>(game.getActions());
        actions.remove(Games.createAction(actionId));

        try {

            SymmetricGameWriter writer = new SymmetricGameWriter(System.out);

            writer.write(new ActionReducedSymmetricGame(game,actions));

        } catch (IOException e) {
            throw new CommandProcessingException(e);
        }
    }

    protected void processStrategicGame(MutableStrategicGame game) throws CommandProcessingException {
        Player player = Games.createPlayer(playerId);
        Set<Action> actions = new HashSet<Action>(game.getActions(player));
        actions.remove(Games.createAction(actionId));

        try {

            StrategicGameWriter writer = new StrategicGameWriter(System.out);

            writer.write(new ActionReducedStrategicGame(game,player,actions));

        } catch (IOException e) {
            throw new CommandProcessingException(e);
        }
    }


    @Override
    protected void addCommandDescription(CommandBuilder commandBuilder) {
        commandBuilder.withDescription("action-reduced game");
    }
}
