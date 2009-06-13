package edu.umich.eecs.ai.egat.cli.ieds;

import edu.umich.eecs.ai.egat.cli.CommandProcessingException;
import edu.umich.eecs.ai.egat.cli.AbstractGameCommandHandler;
import edu.umich.eecs.ai.egat.gamexml.SymmetricGameWriter;
import edu.umich.eecs.ai.egat.gamexml.StrategicGameWriter;
import edu.umich.eecs.ai.egat.game.*;
import edu.umich.eecs.ai.egat.dominance.*;
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
