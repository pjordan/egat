package edu.umich.eecs.ai.egat.cli.mincurb;

import edu.umich.eecs.ai.egat.cli.AbstractGameCommandHandler;
import edu.umich.eecs.ai.egat.cli.CommandProcessingException;
import edu.umich.eecs.ai.egat.cli.regret.SymmetricRegretWriter;
import edu.umich.eecs.ai.egat.game.DefaultSymmetricGame;
import edu.umich.eecs.ai.egat.game.DefaultStrategicGame;
import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.mincurb.BenischMinimumCurbFinder;
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
public class MinCurbCommandHandler extends AbstractGameCommandHandler {
    private Option allOption;

    private boolean all;

    @Override
    protected void addAdditionalChildOptions(GroupBuilder groupBuilder) {
        final DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();

        allOption = defaultOptionBuilder.withLongName("all").withDescription("find all min CURB sets").create();

        groupBuilder.withOption(allOption);
    }


    @Override
    protected void handleAdditionalChildOptions(CommandLine commandLine) throws CommandProcessingException {
        all = commandLine.hasOption(allOption);
    }

    protected String getCommandName() {
        return "min-curb";
    }

    protected void processSymmetricGame(DefaultSymmetricGame game) throws CommandProcessingException {
        SymmetricGameWriter writer = new SymmetricGameWriter(System.out, !all);

        BenischMinimumCurbFinder finder = new BenischMinimumCurbFinder();

        try {
            if(all) {
                System.out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?><nfgs>");
                for(SymmetricGame curb : finder.findAllMinimumCurbs(game)) {
                    writer.write(curb);
                }
                System.out.print("</nfgs>");
            } else {
                writer.write(finder.findMinimumCurb(game));
            }
        } catch (IOException e) {
            throw new CommandProcessingException(e);
        }


    }

    protected void processStrategicGame(DefaultStrategicGame game) throws CommandProcessingException {
        throw new UnsupportedOperationException("min-curb requires a symmetric game");
    }

    @Override
    protected void addCommandDescription(CommandBuilder commandBuilder) {
        commandBuilder.withDescription("compute the min CURB set");
    }
}
