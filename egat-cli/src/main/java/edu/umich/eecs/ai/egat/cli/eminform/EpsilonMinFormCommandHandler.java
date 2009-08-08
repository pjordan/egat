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
package edu.umich.eecs.ai.egat.cli.eminform;

import edu.umich.eecs.ai.egat.cli.AbstractGameCommandHandler;
import edu.umich.eecs.ai.egat.cli.CommandProcessingException;
import edu.umich.eecs.ai.egat.game.*;
import edu.umich.eecs.ai.egat.minform.LpSolveMinimumFormationFinder;
import edu.umich.eecs.ai.egat.minform.LpSolveSymmetricRationalizableFinder;
import edu.umich.eecs.ai.egat.minform.LpSolveStrategicRationalizableFinder;
import edu.umich.eecs.ai.egat.minform.search.SymmetricBestFirstFormationSearch;
import edu.umich.eecs.ai.egat.minform.search.FormationSearchNode;
import edu.umich.eecs.ai.egat.minform.search.StrategicBestFirstFormationSearch;
import edu.umich.eecs.ai.egat.gamexml.SymmetricGameWriter;
import edu.umich.eecs.ai.egat.gamexml.StrategicGameWriter;
import org.apache.commons.cli2.builder.CommandBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;

import java.io.IOException;
import java.util.Set;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public class EpsilonMinFormCommandHandler extends AbstractGameCommandHandler {
    private Option maxSizeOption;

    private int maxSize;

    private Option toleranceOption;

    private double tolerance;

    @Override
    protected void addAdditionalChildOptions(GroupBuilder groupBuilder) {
        final DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();

        final ArgumentBuilder argumentBuilder = new ArgumentBuilder();


        maxSizeOption = defaultOptionBuilder.withLongName("max-size")
                .withArgument(argumentBuilder.withMinimum(1)
                        .withMaximum(1)
                        .withName("max").create())
                .withDescription("minimum game size").create();

        groupBuilder.withOption(maxSizeOption);

        toleranceOption = defaultOptionBuilder.withLongName("tolerance")
                .withArgument(argumentBuilder.withMinimum(1)
                        .withMaximum(1)
                        .withName("tol").create())
                .withDescription("minimum distance").create();

        groupBuilder.withOption(toleranceOption);
    }


    @Override
    protected void handleAdditionalChildOptions(CommandLine commandLine) throws CommandProcessingException {
        maxSize = Integer.parseInt(commandLine.getValue(maxSizeOption).toString());

        if (commandLine.hasOption(toleranceOption)) {
            tolerance = Double.parseDouble(commandLine.getValue(toleranceOption).toString());
        } else {
            tolerance = 1e-8;
        }
    }

    protected String getCommandName() {
        return "emin-form";
    }

    protected void processSymmetricGame(MutableSymmetricGame game) throws CommandProcessingException {
        try {
            SymmetricGameWriter writer = new SymmetricGameWriter(System.out, false);

            LpSolveSymmetricRationalizableFinder finder = new LpSolveSymmetricRationalizableFinder();

            SymmetricBestFirstFormationSearch search = new SymmetricBestFirstFormationSearch(game, finder, tolerance);

            FormationSearchNode<SymmetricGame, Set<Action>> node = search.run(maxSize);

            System.out.print(String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?>"));

            if(node!=null) {
                System.out.print(String.format("<search-node epsilon=\"%f\" size=\"%d\">",node.getValue(), node.getSize()));
                writer.write(node.getGame());
                System.out.print("</search-node>");    
            }
        } catch (NonexistentPayoffException e) {
            System.err.println(String.format("Could not calculate minimum formation. %s", e.getMessage()));
        } catch (IOException e) {
            throw new CommandProcessingException(e);
        }
    }

    protected void processStrategicGame(MutableStrategicGame game) throws CommandProcessingException {
        try {
            StrategicGameWriter writer = new StrategicGameWriter(System.out, false);

            LpSolveStrategicRationalizableFinder finder = new LpSolveStrategicRationalizableFinder();

            StrategicBestFirstFormationSearch search = new StrategicBestFirstFormationSearch(game, finder, tolerance);

            FormationSearchNode<StrategicGame, Map<Player,Set<Action>>> node = search.run(maxSize);

            System.out.print(String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?>"));

            if(node!=null) {
                System.out.print(String.format("<search-node epsilon=\"%f\" size=\"%d\">",node.getValue(), node.getSize()));
                writer.write(node.getGame());
                System.out.print("</search-node>");
            }
        } catch (NonexistentPayoffException e) {
            System.err.println(String.format("Could not calculate minimum formation. %s", e.getMessage()));
        } catch (IOException e) {
            throw new CommandProcessingException(e);
        }
    }

    @Override
    protected void addCommandDescription(CommandBuilder commandBuilder) {
        commandBuilder.withDescription("compute the e-min formation");
    }
}