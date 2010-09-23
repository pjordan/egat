/*
 * IEDSCommandHandler.java
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
package egat.cli.ieds;

import egat.cli.CommandProcessingException;
import egat.cli.AbstractGameCommandHandler;
import egat.gamexml.SymmetricGameWriter;
import egat.gamexml.StrategicGameWriter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.CommandBuilder;

import java.io.IOException;

/**
 * @author Patrick Jordan
 */
public class IEDSCommandHandler extends AbstractGameCommandHandler {
    private Option pureOption;

    private boolean pure;

    @Override
    protected void addAdditionalChildOptions(GroupBuilder groupBuilder) {
        final DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();

        pureOption = defaultOptionBuilder.withShortName("p")
                .withLongName("pure")
                .withDescription("pure stategy dominance").create();

        groupBuilder.withOption(pureOption);
    }


    @Override
    protected void handleAdditionalChildOptions(CommandLine commandLine) throws CommandProcessingException {
        pure = commandLine.hasOption(pureOption);
    }

    protected String getCommandName() {
        return "ieds";
    }

    protected void processSymmetricGame(MutableSymmetricGame game) throws CommandProcessingException {

        try {
            SymmetricDominanceTester dominanceTester = pure ? new PureSymmetricDominanceTesterImpl() : new MixedSymmetricDominanceTesterImpl();

            SymmetricIteratedDominatedStrategiesEliminator eliminator =
                    new SymmetricIteratedDominatedStrategiesEliminatorImpl(dominanceTester);

            eliminator.eliminateDominatedStrategies(game);

            SymmetricGameWriter writer = new SymmetricGameWriter(System.out);

            try {
                writer.write(game);
            } catch (IOException e) {
                throw new CommandProcessingException(e);
            }
        } catch (NonexistentPayoffException e) {
            System.err.println(String.format("Could not calculate regret. %s", e.getMessage()));
        }
    }

    protected void processStrategicGame(MutableStrategicGame game) throws CommandProcessingException {


        try {
            StrategicDominanceTester dominanceTester = pure ? new PureStrategicDominanceTesterImpl() : new MixedStrategicDominanceTesterImpl();

            StrategicIteratedDominatedStrategiesEliminator eliminator =
                    new StrategicIteratedDominatedStrategiesEliminatorImpl(dominanceTester);

            eliminator.eliminateDominatedStrategies(game);

            StrategicGameWriter writer = new StrategicGameWriter(System.out);

            try {
                writer.write(game);
            } catch (IOException e) {
                throw new CommandProcessingException(e);
            }
        } catch (NonexistentPayoffException e) {
            System.err.println(String.format("Could not calculate regret. %s", e.getMessage()));
        }
    }

    @Override
    protected void addCommandDescription(CommandBuilder commandBuilder) {
        commandBuilder.withDescription("remove dominated strategies using IEDS");
    }
}
