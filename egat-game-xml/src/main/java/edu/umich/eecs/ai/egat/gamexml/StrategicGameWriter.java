package edu.umich.eecs.ai.egat.gamexml;

import edu.umich.eecs.ai.egat.game.*;

import java.io.*;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public class StrategicGameWriter extends Writer {
    private PrintWriter writer;

    public StrategicGameWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public StrategicGameWriter(Writer writer) {
        this(new PrintWriter(writer));
    }

    public StrategicGameWriter(OutputStream outputStream) {
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

    public StrategicGameWriter write(StrategicGame game) throws IOException {

        writer.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writer.print(String.format("<nfg name=\"%s\" description=\"%s\">", game.getName(), game.getDescription()));

        writer.print("<players>");
        for (Player player : game.players()) {
            writer.print(String.format("<player id=\"%s\">", player));
            writer.print("<actions>");
            for (Action action : game.getActions(player)) {
                writer.print(String.format("<action id=\"%s\" />", action));
            }
            writer.print("</actions>");
            writer.print("</player>");
        }
        writer.print("</players>");


        writer.print("<payoffs>");
        for (Outcome outcome : Games.traversal(game)) {
            writer.print("<payoff>");

            Payoff payoff = game.payoff(outcome);

            for (Map.Entry<Player, Action> entry : outcome.entrySet()) {
                writer.print(String.format("<outcome player=\"%s\" action=\"%s\" value=\"%s\"/>", entry.getKey(), entry.getValue(), payoff.getPayoff(entry.getKey()).getValue()));
            }

            writer.print("</payoff>");
        }

        writer.print("</payoffs></nfg>");
        writer.flush();

        return this;
    }
}
