package edu.umich.eecs.ai.egat.cli.regret;

import edu.umich.eecs.ai.egat.cli.CommandProcessingException;
import edu.umich.eecs.ai.egat.cli.AbstractGameCommandHandler;
import edu.umich.eecs.ai.egat.game.*;
import org.apache.commons.cli2.builder.CommandBuilder;


/**
 * @author Patrick Jordan
 */
public class RegretCommandHandler extends AbstractGameCommandHandler {

    protected String getCommandName() {
        return "regret";
    }

    protected void processSymmetricGame(MutableSymmetricGame game) throws CommandProcessingException {
        SymmetricRegretWriter writer = new SymmetricRegretWriter(System.out);

        writer.writeRegret(game);

    }

    protected void processStrategicGame(MutableStrategicGame game) throws CommandProcessingException {
        StrategicRegretWriter writer = new StrategicRegretWriter(System.out);

        writer.writeRegret(game);
    }

    @Override
    protected void addCommandDescription(CommandBuilder commandBuilder) {
        commandBuilder.withDescription("compute the regret of each (pure) profile");
    }
}
