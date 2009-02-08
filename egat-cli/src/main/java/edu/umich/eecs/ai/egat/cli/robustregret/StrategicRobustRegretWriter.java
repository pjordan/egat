package edu.umich.eecs.ai.egat.cli.robustregret;

import edu.umich.eecs.ai.egat.game.*;

import java.io.PrintStream;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public class StrategicRobustRegretWriter {
    private PrintStream printStream;

    public StrategicRobustRegretWriter(PrintStream printStream) {
        this.printStream = printStream;
    }

    public PrintStream writeRegret(StrategicGame game) {
        writeHeader();

        for(Player player : game.players()) {
            writeRegret(player, game );
        }


        writeFooter().flush();

        return printStream;
    }

    protected PrintStream writeRegret(Player player, StrategicGame game) {
        writePlayerHeader(player);

        for(Action action : game.getActions(player)) {
            writeRegret(action, Games.robustRegret(player, action, game) );
        }

        writePlayerFooter(player);

        return printStream;
    }

    protected PrintStream writeRegret(Action action, double regret) {
        printStream.print(String.format("<action id=\"%s\" regret=\"%s\" />", action, regret));

        return printStream;
    }

    protected PrintStream writeHeader() {
        printStream.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        printStream.print("<players>");

        return printStream;
    }
   
    protected PrintStream writeFooter() {
        printStream.print("</players>");

        return printStream;
    }

    protected PrintStream writePlayerHeader(Player player) {
        printStream.print(String.format("<player id=\"%s\">", player.getID()));

        return printStream;
    }

    protected PrintStream writePlayerFooter(Player player) {
        printStream.print("</player>");

        return printStream;
    }
}
