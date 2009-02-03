package edu.umich.eecs.ai.egat.gamexml;

import edu.umich.eecs.ai.egat.game.*;

import java.io.*;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public class SymmetricGameWriter extends Writer {
    private PrintWriter writer;

    public SymmetricGameWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public SymmetricGameWriter(Writer writer) {
        this(new PrintWriter(writer));
    }

    public SymmetricGameWriter(OutputStream outputStream) {
        this(new OutputStreamWriter(outputStream));
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    public void flush() throws IOException {
        writer.flush();
    }

    public void close() throws IOException {
        writer.close();
    }

    public SymmetricGameWriter write(SymmetricGame game) throws IOException {

        writer.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writer.print(String.format("<nfg name=\"%s\" description=\"%s\">", game.getName(), game.getDescription()));

        writer.print("<players>");
        for(Player player : game.players()) {
            writer.print(String.format("<player id=\"%s\" />",player));
        }
        writer.print("</players>");

        writer.print("<actions>");
        for(Action action : game.getActions()) {
            writer.print(String.format("<action id=\"%s\" />",action));
        }
        writer.print("</actions>");


        writer.print("<payoffs>");
        for (SymmetricOutcome outcome : Games.symmetricTraversal(game)) {
                writer.print("<payoff>");

                SymmetricPayoff payoff = game.payoff(outcome);

                for (Map.Entry<Action, Integer> entry : outcome.actionEntrySet()) {
                    writer.print(String.format("<outcome action=\"%s\" count=\"%s\" value=\"%s\"/>", entry.getKey(), entry.getValue(), payoff.getPayoff(entry.getKey()).getValue()));
                }

                writer.print("</payoff>");
            }

        writer.print("</payoffs></nfg>");
        flush();

        return this;
    }
}
