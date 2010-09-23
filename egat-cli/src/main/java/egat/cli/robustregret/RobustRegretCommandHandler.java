/*
 * RobustRegretCommandHandler.java
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
package egat.cli.robustregret;

import egat.cli.AbstractGameCommandHandler;
import egat.cli.CommandProcessingException;
import egat.game.MutableStrategicGame;
import egat.game.MutableSymmetricGame;
import egat.game.NonexistentPayoffException;
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
