package edu.umich.eecs.ai.egat.cli.regret;


import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.SymmetricOutcome;
import edu.umich.eecs.ai.egat.game.Games;
import edu.umich.eecs.ai.egat.game.Action;

import java.io.PrintStream;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public class SymmetricRegretWriter {
    private PrintStream printStream;

    public SymmetricRegretWriter(PrintStream printStream) {
        this.printStream = printStream;
    }

    public PrintStream writeRegret(SymmetricGame game) {
        writeHeader();

        for (SymmetricOutcome outcome : Games.symmetricTraversal(game)) {
            writeRegret(outcome, Games.regret(outcome, game) );
        }

        writeFooter().flush();

        return printStream;
    }

    protected PrintStream writeRegret(SymmetricOutcome outcome, double regret) {
        printStream.print(String.format("<profile regret=\"%s\">", regret));

        for (Map.Entry<Action, Integer> entry : outcome.actionEntrySet()) {
            printStream.print(String.format("<outcome action=\"%s\" count=\"%s\" />", entry.getKey(), entry.getValue()));
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
