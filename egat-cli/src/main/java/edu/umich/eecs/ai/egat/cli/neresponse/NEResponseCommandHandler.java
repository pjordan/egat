package edu.umich.eecs.ai.egat.cli.neresponse;

import edu.umich.eecs.ai.egat.cli.CommandProcessingException;
import edu.umich.eecs.ai.egat.cli.AbstractGameCommandHandler;
import edu.umich.eecs.ai.egat.game.*;
import edu.umich.eecs.ai.egat.gamexml.StrategyHandler;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.CommandBuilder;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Patrick Jordan
 */
public class NEResponseCommandHandler extends AbstractGameCommandHandler {
    private Option playerOption;

    private String playerId;

    private Option profilePathOption;

    private String profilePath;

    private Option gainOption;

    private boolean gain;

    @Override
    protected void addAdditionalChildOptions(GroupBuilder groupBuilder) {
        final DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();
        final ArgumentBuilder argumentBuilder = new ArgumentBuilder();


        playerOption = defaultOptionBuilder.withLongName("player")
                                           .withArgument(argumentBuilder.withMinimum(1)
                                                                        .withMaximum(1)
                                                                        .withName("id").create())
                                           .withDescription("id of player (non-symmetric) to compute response").create();

        groupBuilder.withOption(playerOption);

        profilePathOption = defaultOptionBuilder.withLongName("profile-path")
                                                .withArgument(argumentBuilder.withMinimum(1)
                                                                             .withMaximum(1)
                                                                             .withName("path").create())
                                                .withDescription("path of profile (strategy with -sym) used as background context").create();

        groupBuilder.withOption(profilePathOption);

        gainOption = defaultOptionBuilder.withLongName("gain")
                                         .withDescription("compute gain instead of payoff").create();

        groupBuilder.withOption(gainOption);
    }


    @Override
    protected void handleAdditionalChildOptions(CommandLine commandLine) throws CommandProcessingException {
        if (commandLine.hasOption(playerOption)) {
            playerId = (String) commandLine.getValue(playerOption);
        }

        profilePath = (String) commandLine.getValue(profilePathOption);

        gain = commandLine.hasOption(gainOption);
    }

    protected String getCommandName() {
        return "ne-response";
    }

    protected void processSymmetricGame(MutableSymmetricGame game) throws CommandProcessingException {

        InputStream inputStream = null;
        try {

            inputStream = new FileInputStream(profilePath);

        } catch (FileNotFoundException e) {

            throw new CommandProcessingException(e);

        }

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();

            SAXParser parser = factory.newSAXParser();

            StrategyHandler handler = new StrategyHandler();

            parser.parse(inputStream, handler);

            Strategy strategy = handler.getStrategy();

            findNEResponse(strategy, game);
        } catch (NonexistentPayoffException e) {
            System.err.println(String.format("Could not calculate regret. %s", e.getMessage()));
        } catch (ParserConfigurationException e) {
            throw new CommandProcessingException(e);
        } catch (SAXException e) {
            throw new CommandProcessingException(e);
        } catch (IOException e) {
            throw new CommandProcessingException(e);
        }
    }

    protected void processStrategicGame(MutableStrategicGame game) throws CommandProcessingException {
        throw new UnsupportedOperationException("NE-response is defined");
    }

    protected void findNEResponse(Strategy strategy, SymmetricGame game) {
        Player[] players = game.players().toArray(new Player[0]);
        Strategy[] strategies = new Strategy[players.length];
        Arrays.fill(strategies, strategy);

        System.out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        System.out.print("<ne-response>");
        double basePayoff = 0.0;
        if(gain) {
            basePayoff = game.payoff(Games.createProfile(players, strategies)).getPayoff(players[0]).getValue();
        }
        for(Action action : game.getActions()) {
            strategies[0] = Games.createStrategy(new Action[]{action}, new Number[]{1.0});
            double response = game.payoff(Games.createProfile(players, strategies)).getPayoff(players[0]).getValue();

            if(gain) {
                System.out.print(String.format("<action id=\"%s\" gain=\"%f\" />",action.getID(), response - basePayoff));
            } else {
                System.out.print(String.format("<action id=\"%s\" payoff=\"%f\" />",action.getID(), response));
            }
        }

        System.out.print("</ne-response>");
    }

    @Override
    protected void addCommandDescription(CommandBuilder commandBuilder) {
        commandBuilder.withDescription("compute the NE-response of each strategy");
    }
}
