/*
 * SymmetricGameWriter.java
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
public class SymmetricGameWriter extends Writer {
    private PrintWriter writer;

    private boolean header;

    public SymmetricGameWriter(PrintWriter writer) {
        this(writer,true);
    }

    public SymmetricGameWriter(Writer writer) {
        this(new PrintWriter(writer));
    }

    public SymmetricGameWriter(Writer writer, boolean header) {
        this(new PrintWriter(writer), header);
    }
    public SymmetricGameWriter(OutputStream outputStream) {
        this(new OutputStreamWriter(outputStream));
    }

    public SymmetricGameWriter(OutputStream outputStream, boolean header) {
        this(new OutputStreamWriter(outputStream), header);
    }

    public SymmetricGameWriter(PrintWriter writer, boolean header) {
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

    public SymmetricGameWriter write(SymmetricGame game) throws IOException {

        if(header) {
            writer.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        }
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
