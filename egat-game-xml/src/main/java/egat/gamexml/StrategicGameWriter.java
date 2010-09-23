/*
 * StrategicGameWriter.java
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
package egat.gamexml;

import egat.game.*;

import java.io.*;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public class StrategicGameWriter extends Writer {
    private PrintWriter writer;

    private boolean header;

    public StrategicGameWriter(PrintWriter writer) {
        this(writer, true);
    }

    public StrategicGameWriter(Writer writer) {
        this(new PrintWriter(writer));
    }

    public StrategicGameWriter(Writer writer, boolean header) {
        this(new PrintWriter(writer), header);
    }

    public StrategicGameWriter(OutputStream outputStream) {
        this(new OutputStreamWriter(outputStream));
    }

    public StrategicGameWriter(OutputStream outputStream, boolean header) {
        this(new OutputStreamWriter(outputStream), header);
    }

    public StrategicGameWriter(PrintWriter writer, boolean header) {
        this.writer = writer;
        this.header = header;
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

        if(header) {
            writer.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        }
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
