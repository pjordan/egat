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
package egat.cli.eminform;

import egat.cli.AbstractGameCommandHandler;
import egat.cli.CommandProcessingException;
import egat.minform.LpSolveSymmetricRationalizableFinder;
import egat.minform.LpSolveStrategicRationalizableFinder;
import egat.gamexml.SymmetricGameWriter;
import egat.gamexml.StrategicGameWriter;
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
    private Option breadthOption;

    private boolean breadth;

    private Option epsilonGreedyOption;

    private boolean epsilonGreedy;

    private Option tauGreedyOption;

    private boolean tauGreedy;

    private Option maxSizeOption;

    private int maxSize;

    private Option maxQueueOption;

    private int maxQueue;

    private Option toleranceOption;

    private double tolerance;

    @Override
    protected void addAdditionalChildOptions(GroupBuilder groupBuilder) {
        final DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();

        final ArgumentBuilder argumentBuilder = new ArgumentBuilder();

        breadthOption = defaultOptionBuilder.withLongName("breadth").withDescription("run the breadth-first algorithm").create();

        groupBuilder.withOption(breadthOption);

        epsilonGreedyOption = defaultOptionBuilder.withLongName("epsilon-greedy").withDescription("run the epsilon greedy algorithm").create();

        groupBuilder.withOption(epsilonGreedyOption);

        tauGreedyOption = defaultOptionBuilder.withLongName("tau-greedy").withDescription("run the tau greedy algorithm").create();

        groupBuilder.withOption(tauGreedyOption);

        maxSizeOption = defaultOptionBuilder.withLongName("max-size")
                .withArgument(argumentBuilder.withMinimum(1)
                        .withMaximum(1)
                        .withName("max").create())
                .withDescription("minimum game size").create();

        groupBuilder.withOption(maxSizeOption);

        maxQueueOption = defaultOptionBuilder.withLongName("max-queue")
                .withArgument(argumentBuilder.withMinimum(1)
                        .withMaximum(1)
                        .withName("max").create())
                .withDescription("max queue size").create();

        groupBuilder.withOption(maxQueueOption);

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

        if (commandLine.hasOption(maxQueueOption)) {
            maxQueue = Integer.parseInt(commandLine.getValue(maxQueueOption).toString());
        } else {
            maxQueue = Integer.MAX_VALUE;
        }

        breadth = commandLine.hasOption(breadthOption);
        epsilonGreedy = commandLine.hasOption(epsilonGreedyOption);
        tauGreedy = commandLine.hasOption(tauGreedyOption);
    }

    protected String getCommandName() {
        return "emin-form";
    }

    protected void processSymmetricGame(MutableSymmetricGame game) throws CommandProcessingException {
        try {
            SymmetricGameWriter writer = new SymmetricGameWriter(System.out, false);

            LpSolveSymmetricRationalizableFinder finder = new LpSolveSymmetricRationalizableFinder();

            FormationSearch<SymmetricGame, SymmetricMultiAgentSystem, Set<Action>> search;

            if(breadth) {
                search = new SymmetricBreadthFirstFormationSearch(game, finder, maxQueue, tolerance);
            } else if(epsilonGreedy) {
                search = new SymmetricEpsilonGreedyFormationSearch(game, finder, tolerance);
            } else if(tauGreedy) {
                search = new SymmetricTauGreedyFormationSearch(game, finder, tolerance);
            } else {
                search = new SymmetricBestFirstFormationSearch(game, finder, maxQueue, tolerance);
            }

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

            FormationSearch<StrategicGame, StrategicMultiAgentSystem, Map<Player, Set<Action>>> search;

            if(breadth) {
                search = new StrategicBreadthFirstFormationSearch(game, finder, maxQueue, tolerance);
            } else if(epsilonGreedy) {
                search = new StrategicEpsilonGreedyFormationSearch(game, finder, tolerance);
            } else if(tauGreedy) {
                search = new StrategicTauGreedyFormationSearch(game, finder, tolerance);
            } else {
                search = new StrategicBestFirstFormationSearch(game, finder, maxQueue, tolerance);
            }


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