/*
 * MinFormCommandHandler.java
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
package edu.umich.eecs.ai.egat.cli.minform;

import edu.umich.eecs.ai.egat.cli.AbstractGameCommandHandler;
import edu.umich.eecs.ai.egat.cli.CommandProcessingException;
import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.MutableStrategicGame;
import edu.umich.eecs.ai.egat.game.MutableSymmetricGame;
import edu.umich.eecs.ai.egat.game.NonexistentPayoffException;
import edu.umich.eecs.ai.egat.minform.LpSolveMinimumFormationFinder;
import edu.umich.eecs.ai.egat.gamexml.SymmetricGameWriter;
import org.apache.commons.cli2.builder.CommandBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;

import java.io.IOException;

/**
 * @author Patrick Jordan
 */
public class MinFormCommandHandler extends AbstractGameCommandHandler {
    private Option allOption;

    private boolean all;

    @Override
    protected void addAdditionalChildOptions(GroupBuilder groupBuilder) {
        final DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();

        allOption = defaultOptionBuilder.withLongName("all").withDescription("find all min formations").create();

        groupBuilder.withOption(allOption);
    }


    @Override
    protected void handleAdditionalChildOptions(CommandLine commandLine) throws CommandProcessingException {
        all = commandLine.hasOption(allOption);
    }

    protected String getCommandName() {
        return "min-form";
    }

    protected void processSymmetricGame(MutableSymmetricGame game) throws CommandProcessingException {
        try {
            SymmetricGameWriter writer = new SymmetricGameWriter(System.out, !all);

            LpSolveMinimumFormationFinder finder = new LpSolveMinimumFormationFinder();


            if (all) {
                System.out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?><nfgs>");
                for (SymmetricGame form : finder.findAllMinimumFormations(game)) {
                    writer.write(form);
                }
                System.out.print("</nfgs>");
            } else {
                writer.write(finder.findMinimumFormation(game));
            }
        } catch (NonexistentPayoffException e) {
            System.err.println(String.format("Could not calculate regret. %s", e.getMessage()));
        } catch (IOException e) {
            throw new CommandProcessingException(e);
        }


    }

    protected void processStrategicGame(MutableStrategicGame game) throws CommandProcessingException {
        throw new UnsupportedOperationException("min-form requires a symmetric game");
    }

    @Override
    protected void addCommandDescription(CommandBuilder commandBuilder) {
        commandBuilder.withDescription("compute the min formation");
    }
}
