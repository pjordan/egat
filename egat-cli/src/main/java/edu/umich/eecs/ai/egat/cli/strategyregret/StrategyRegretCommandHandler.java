package edu.umich.eecs.ai.egat.cli.strategyregret;

import edu.umich.eecs.ai.egat.cli.AbstractGameCommandHandler;
import edu.umich.eecs.ai.egat.cli.CommandProcessingException;
import edu.umich.eecs.ai.egat.game.*;
import edu.umich.eecs.ai.egat.gamexml.ProfileHandler;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.CommandBuilder;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * @author Patrick Jordan
 */
public class StrategyRegretCommandHandler extends AbstractGameCommandHandler {
    private Option playerOption;

    private String playerId;

    private Option profilePathOption;

    private String profilePath;

    private Option uniformOption;

    private boolean uniform;

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

        uniformOption = defaultOptionBuilder.withLongName("uniform")
                .withDescription("use a uniform profile").create();

        groupBuilder.withOption(uniformOption);
    }


    @Override
    protected void handleAdditionalChildOptions(CommandLine commandLine) throws CommandProcessingException {
        if (commandLine.hasOption(playerOption)) {
            playerId = (String) commandLine.getValue(playerOption);
        }

        profilePath = (String) commandLine.getValue(profilePathOption);

        uniform = commandLine.hasOption(uniformOption);
    }

    protected String getCommandName() {
        return "strategy-regret";
    }

    protected void processSymmetricGame(MutableSymmetricGame game) throws CommandProcessingException {

        try {
            Profile profile = null;

            if (uniform) {
                Player[] players = game.players().toArray(new Player[0]);
                Strategy[] strategies = new Strategy[players.length];

                Action[] actions = ((Set<Action>) game.getActions()).toArray(new Action[0]);
                Number[] distribution = new Number[actions.length];

                Arrays.fill(distribution, 1.0 / distribution.length);

                Strategy strategy = Games.createStrategy(actions, distribution);
                Arrays.fill(strategies, strategy);

                profile = Games.createProfile(players, strategies);
            } else {
                InputStream inputStream = null;

                inputStream = new FileInputStream(profilePath);

                SAXParserFactory factory = SAXParserFactory.newInstance();

                SAXParser parser = factory.newSAXParser();

                ProfileHandler handler = new ProfileHandler();

                parser.parse(inputStream, handler);

                profile = handler.getProfile();
            }

            findRegret(profile, game);
        } catch (NonexistentPayoffException e) {
            System.err.println(String.format("Could not calculate regret. %s", e.getMessage()));
        } catch (FileNotFoundException e) {
            throw new CommandProcessingException(e);
        } catch (ParserConfigurationException e) {
            throw new CommandProcessingException(e);
        } catch (SAXException e) {
            throw new CommandProcessingException(e);
        } catch (IOException e) {
            throw new CommandProcessingException(e);
        }
    }

    protected void processStrategicGame(MutableStrategicGame game) throws CommandProcessingException {
        try {
            Profile profile = null;

            if (uniform) {
                Player[] players = game.players().toArray(new Player[0]);
                Strategy[] strategies = new Strategy[players.length];

                for (int i = 0; i < players.length; i++) {
                    Action[] actions = ((Set<Action>) game.getActions(players[i])).toArray(new Action[0]);
                    Number[] distribution = new Number[actions.length];
                    Arrays.fill(distribution, 1.0 / distribution.length);
                    strategies[i] = Games.createStrategy(actions, distribution);
                }

                profile = Games.createProfile(players, strategies);

            } else {
                InputStream inputStream = null;

                inputStream = new FileInputStream(profilePath);

                SAXParserFactory factory = SAXParserFactory.newInstance();

                SAXParser parser = factory.newSAXParser();

                ProfileHandler handler = new ProfileHandler();

                parser.parse(inputStream, handler);

                profile = handler.getProfile();
            }

            findRegret(profile, game);
        } catch (NonexistentPayoffException e) {
            System.err.println(String.format("Could not calculate regret. %s", e.getMessage()));
        } catch (FileNotFoundException e) {
            throw new CommandProcessingException(e);
        } catch (ParserConfigurationException e) {
            throw new CommandProcessingException(e);
        } catch (SAXException e) {
            throw new CommandProcessingException(e);
        } catch (IOException e) {
            throw new CommandProcessingException(e);
        }
    }

    protected void findRegret(Profile profile, SymmetricGame game) {
        Player[] players = profile.players().toArray(new Player[0]);
        Strategy[] strategies = new Strategy[players.length];

        for (int i = 0; i < players.length; i++) {
            strategies[i] = profile.getStrategy(players[i]);
        }

        int playerIndex = 0;

        if (playerId != null) {
            for (int i = 0; i < players.length; i++) {
                if (playerId.equals(players[i].getID())) {
                    playerIndex = i;
                    break;
                }
            }
        }

        System.out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        System.out.print("<strategy-regret>");

        double maxPayoff = Double.NEGATIVE_INFINITY;

        Action[] actions = game.getActions().toArray(new Action[0]);
        double[] payoffs = new double[actions.length];

        for (int i = 0; i < actions.length; i++) {
            strategies[playerIndex] = Games.createStrategy(new Action[]{actions[i]}, new Number[]{1.0});
            double response = game.payoff(Games.createProfile(players, strategies)).getPayoff(players[0]).getValue();
            payoffs[i] = response;
            maxPayoff = Math.max(response, maxPayoff);
        }

        for (int i = 0; i < actions.length; i++) {
            System.out.print(String.format("<action id=\"%s\" regret=\"%f\" />", actions[i].getID(), maxPayoff - payoffs[i]));
        }

        System.out.print("</strategy-regret>");
    }

    protected void findRegret(Profile profile, StrategicGame game) {
            Player[] players = profile.players().toArray(new Player[0]);
            Strategy[] strategies = new Strategy[players.length];

            for (int i = 0; i < players.length; i++) {
                strategies[i] = profile.getStrategy(players[i]);
            }

            int playerIndex = 0;

            if (playerId != null) {
                for (int i = 0; i < players.length; i++) {
                    if (playerId.equals(players[i].getID())) {
                        playerIndex = i;
                        break;
                    }
                }
            }

            System.out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            System.out.print(String.format("<strategy-regret player=\"%s\">", players[playerIndex].getID()));


            double maxPayoff = Double.NEGATIVE_INFINITY;

            Action[] actions = game.getActions(players[playerIndex]).toArray(new Action[0]);
            double[] payoffs = new double[actions.length];

            for (int i = 0; i < actions.length; i++) {
                strategies[playerIndex] = Games.createStrategy(new Action[]{actions[i]}, new Number[]{1.0});
                double response = game.payoff(Games.createProfile(players, strategies)).getPayoff(players[0]).getValue();
                payoffs[i] = response;
                maxPayoff = Math.max(response, maxPayoff);
            }

            for (int i = 0; i < actions.length; i++) {
                System.out.print(String.format("<action id=\"%s\" regret=\"%f\" />", actions[i].getID(), maxPayoff - payoffs[i]));
            }

            System.out.print("</strategy-regret>");
        }

    @Override
    protected void addCommandDescription(CommandBuilder commandBuilder) {
        commandBuilder.withDescription("regret of each strategy");
    }
}
