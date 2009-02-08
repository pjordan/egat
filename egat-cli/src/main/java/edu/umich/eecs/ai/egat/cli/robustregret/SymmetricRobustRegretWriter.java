package edu.umich.eecs.ai.egat.cli.robustregret;

import edu.umich.eecs.ai.egat.game.*;

import java.io.PrintStream;

/**
 * @author Patrick Jordan
 */
public class SymmetricRobustRegretWriter {
    private PrintStream printStream;

    public SymmetricRobustRegretWriter(PrintStream printStream) {
        this.printStream = printStream;
    }

    public PrintStream writeRegret(SymmetricGame game) {
        writeHeader();

        for(Action action : game.getActions()) {
            writeRegret(action, Games.robustRegret(action, game) );
        }

        writeFooter().flush();

        return printStream;
    }

    protected PrintStream writeRegret(Action action, double regret) {
        printStream.print(String.format("<action id=\"%s\" regret=\"%s\" />", action, regret));

        return printStream;
    }

    protected PrintStream writeHeader() {
        printStream.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        printStream.print("<actions>");

        return printStream;
    }

    protected PrintStream writeFooter() {
        printStream.print("</actions>");

        return printStream;
    }
}
