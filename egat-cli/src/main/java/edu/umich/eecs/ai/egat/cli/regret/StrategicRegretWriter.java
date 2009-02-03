package edu.umich.eecs.ai.egat.cli.regret;

import edu.umich.eecs.ai.egat.game.*;

import java.io.PrintStream;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public class StrategicRegretWriter {
    private PrintStream printStream;

    public StrategicRegretWriter(PrintStream printStream) {
        this.printStream = printStream;
    }

    public PrintStream writeRegret(StrategicGame game) {
        writeHeader();

        for (Outcome outcome : Games.traversal(game)) {
            writeRegret(outcome, Games.regret(outcome, game) );
        }

        writeFooter().flush();

        return printStream;
    }

    protected PrintStream writeRegret(Outcome outcome, double regret) {
        printStream.print(String.format("<profile regret=\"%s\">", regret));

        for (Map.Entry<Player, Action> entry : outcome.entrySet()) {
            printStream.print(String.format("<outcome player=\"%s\"  action=\"%s\" />", entry.getKey(), entry.getValue()));
        }

        printStream.print("</profile>");

        return printStream;
    }

    protected PrintStream writeHeader() {
        printStream.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        printStream.print("<profiles>");

        return printStream;
    }

    protected PrintStream writeFooter() {
        printStream.print("</profiles>");

        return printStream;
    }
}
