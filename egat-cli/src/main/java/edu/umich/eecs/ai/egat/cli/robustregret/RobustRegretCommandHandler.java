package edu.umich.eecs.ai.egat.cli.robustregret;

import edu.umich.eecs.ai.egat.cli.AbstractGameCommandHandler;
import edu.umich.eecs.ai.egat.cli.CommandProcessingException;
import edu.umich.eecs.ai.egat.game.MutableStrategicGame;
import edu.umich.eecs.ai.egat.game.MutableSymmetricGame;
import edu.umich.eecs.ai.egat.game.NonexistentPayoffException;
import org.apache.commons.cli2.builder.CommandBuilder;

/**
 * @author Patrick Jordan
 */
public class RobustRegretCommandHandler extends AbstractGameCommandHandler {

    protected String getCommandName() {
        return "robust-regret";
    }

    protected void processSymmetricGame(MutableSymmetricGame game) throws CommandProcessingException {
        try {
            SymmetricRobustRegretWriter writer = new SymmetricRobustRegretWriter(System.out);


            writer.writeRegret(game);
        } catch (NonexistentPayoffException e) {
            System.err.println(String.format("Could not calculate regret. %s", e.getMessage()));
        }
    }

    protected void processStrategicGame(MutableStrategicGame game) throws CommandProcessingException {
        try {
            StrategicRobustRegretWriter writer = new StrategicRobustRegretWriter(System.out);

            writer.writeRegret(game);
        } catch (NonexistentPayoffException e) {
            System.err.println(String.format("Could not calculate regret. %s", e.getMessage()));
        }
    }

    @Override
    protected void addCommandDescription(CommandBuilder commandBuilder) {
        commandBuilder.withDescription("compute the regret of each action");
    }
}
