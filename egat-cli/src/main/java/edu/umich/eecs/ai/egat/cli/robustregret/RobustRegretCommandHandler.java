package edu.umich.eecs.ai.egat.cli.robustregret;

import edu.umich.eecs.ai.egat.cli.AbstractGameCommandHandler;
import edu.umich.eecs.ai.egat.cli.CommandProcessingException;
import edu.umich.eecs.ai.egat.game.MutableStrategicGame;
import edu.umich.eecs.ai.egat.game.MutableSymmetricGame;
import org.apache.commons.cli2.builder.CommandBuilder;

/**
 * @author Patrick Jordan
 */
public class RobustRegretCommandHandler extends AbstractGameCommandHandler {

    protected String getCommandName() {
        return "robust-regret";
    }

    protected void processSymmetricGame(MutableSymmetricGame game) throws CommandProcessingException {
        SymmetricRobustRegretWriter writer = new SymmetricRobustRegretWriter(System.out);

        writer.writeRegret(game);

    }

    protected void processStrategicGame(MutableStrategicGame game) throws CommandProcessingException {
        StrategicRobustRegretWriter writer = new StrategicRobustRegretWriter(System.out);

        writer.writeRegret(game);
    }

    @Override
    protected void addCommandDescription(CommandBuilder commandBuilder) {
        commandBuilder.withDescription("compute the regret of each action");
    }
}
