package edu.umich.eecs.ai.egat.cli.ieds;

import edu.umich.eecs.ai.egat.cli.CommandProcessingException;
import edu.umich.eecs.ai.egat.cli.AbstractGameCommandHandler;
import edu.umich.eecs.ai.egat.gamexml.SymmetricGameWriter;
import edu.umich.eecs.ai.egat.gamexml.StrategicGameWriter;
import edu.umich.eecs.ai.egat.game.*;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.CommandBuilder;

import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Patrick Jordan
 */
public class IEDSCommandHandler extends AbstractGameCommandHandler {
    private Option verboseOption;

    private boolean verbose;

    @Override
    protected void addAdditionalChildOptions(GroupBuilder groupBuilder) {
        final DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();

        verboseOption = defaultOptionBuilder.withShortName("v")
                                            .withLongName("verbose")
                                            .withDescription("print algorithm details").create();

        groupBuilder.withOption(verboseOption);
    }


    @Override
    protected void handleAdditionalChildOptions(CommandLine commandLine) throws CommandProcessingException {
        verbose = commandLine.hasOption(verboseOption);
    }

    protected String getCommandName() {
        return "ieds";
    }

    protected void processSymmetricGame(DefaultSymmetricGame game) throws CommandProcessingException {
        PrintStream stream = null;

        if (verbose) {
            stream = System.err;
        }

        SymmetricIteratedDominatedStrategyRemover remover = new SymmetricIteratedDominatedStrategyRemover(stream);

        remover.removeDominatedStrategies(game);

        SymmetricGameWriter writer = new SymmetricGameWriter(System.out);

        try {
            writer.write(game);
        } catch (IOException e) {
            throw new CommandProcessingException(e);
        }

    }

    protected void processStrategicGame(DefaultStrategicGame game) throws CommandProcessingException {
        PrintStream stream = null;

        if (verbose) {
            stream = System.err;
        }

        IteratedDominatedStrategyRemover remover = new IteratedDominatedStrategyRemover(stream);

        remover.removeDominatedStrategies(game);

        StrategicGameWriter writer = new StrategicGameWriter(System.out);

        try {
            writer.write(game);
        } catch (IOException e) {
            throw new CommandProcessingException(e);
        }

    }

    @Override
    protected void addCommandDescription(CommandBuilder commandBuilder) {
        commandBuilder.withDescription("remove dominated strategies using IEDS");
    }
}
