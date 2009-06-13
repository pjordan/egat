package edu.umich.eecs.ai.egat.cli.replicatordynamics;

import edu.umich.eecs.ai.egat.cli.AbstractGameCommandHandler;
import edu.umich.eecs.ai.egat.cli.CommandProcessingException;
import edu.umich.eecs.ai.egat.game.*;
import edu.umich.eecs.ai.egat.gamexml.ProfileWriter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.CommandBuilder;

import java.io.PrintStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Patrick Jordan
 */
public class ReplicatorDynamicsCommandHandler extends AbstractGameCommandHandler {
    private Option verboseOption;

    private boolean verbose;

    private Option maxIterationOption;

    private int maxIteration;

    private Option toleranceOption;

    private double tolerance;

    @Override
    protected void addAdditionalChildOptions(GroupBuilder groupBuilder) {
        final DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();
        final ArgumentBuilder argumentBuilder = new ArgumentBuilder();

        verboseOption = defaultOptionBuilder.withShortName("v")
                .withLongName("verbose")
                .withDescription("prints info for each iteration").create();

        groupBuilder.withOption(verboseOption);

        toleranceOption = defaultOptionBuilder.withLongName("tolerance")
                .withArgument(argumentBuilder.withMinimum(1)
                        .withMaximum(1)
                        .withName("tol").create())
                .withDescription("minimum update L-infinity distance").create();

        groupBuilder.withOption(toleranceOption);

        maxIterationOption = defaultOptionBuilder.withLongName("max-iterations")
                .withArgument(argumentBuilder.withMinimum(1)
                        .withMaximum(1)
                        .withName("i").create())
                .withDescription("maximum iterations to run").create();

        groupBuilder.withOption(maxIterationOption);
    }


    @Override
    protected void handleAdditionalChildOptions(CommandLine commandLine) throws CommandProcessingException {
        verbose = commandLine.hasOption(verboseOption);

        tolerance = Double.parseDouble(commandLine.getValue(toleranceOption).toString());

        maxIteration = Integer.parseInt(commandLine.getValue(maxIterationOption).toString());
    }

    protected void processSymmetricGame(MutableSymmetricGame game) throws CommandProcessingException {
        try {
            PrintStream printStream = null;

            if (verbose) {
                printStream = System.err;
            }


            SymmetricReplicatorDynamics symmetricReplicatorDynamics = new SymmetricReplicatorDynamics(tolerance, maxIteration, printStream);
            Strategy strategy = symmetricReplicatorDynamics.run(game, null);
            Player[] players = game.players().toArray(new Player[0]);
            Strategy[] strategies = new Strategy[players.length];
            Arrays.fill(strategies, strategy);

            ProfileWriter writer = new ProfileWriter(System.out);
            try {
                writer.write(Games.createProfile(players, strategies));
            } catch (IOException e) {
                throw new CommandProcessingException(e);
            }
        } catch (NonexistentPayoffException e) {
            System.err.println(String.format("Could not calculate regret. %s", e.getMessage()));
        }


    }

    protected void processStrategicGame(MutableStrategicGame game) throws CommandProcessingException {
        throw new UnsupportedOperationException("replicator dynamics requires a symmetric game");
    }

    protected String getCommandName() {
        return "rd";
    }

    @Override
    protected void addCommandDescription(CommandBuilder commandBuilder) {
        commandBuilder.withDescription("find sample NE using replicator dynamics");
    }
}
