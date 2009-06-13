/*
 * StrategyWriter.java
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
package edu.umich.eecs.ai.egat.gamexml;

import edu.umich.eecs.ai.egat.game.Strategy;
import edu.umich.eecs.ai.egat.game.Action;

import java.io.*;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public class StrategyWriter extends Writer {
    private PrintWriter writer;

    private boolean header;

    public StrategyWriter(PrintWriter writer) {
        this(writer, true);
    }

    public StrategyWriter(Writer writer) {
        this(new PrintWriter(writer));
    }

    public StrategyWriter(Writer writer, boolean header) {
        this(new PrintWriter(writer), header);
    }

    public StrategyWriter(OutputStream outputStream) {
        this(new OutputStreamWriter(outputStream));
    }

    public StrategyWriter(OutputStream outputStream, boolean header) {
        this(new OutputStreamWriter(outputStream), header);
    }

    public StrategyWriter(PrintWriter writer, boolean header) {
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

    public StrategyWriter write(Strategy strategy) throws IOException {

        if(header) {
            writer.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        }
        writer.print("<strategy>");


        for(Map.Entry<Action,Number> entry : strategy.entrySet()) {
            writer.print(String.format("<action id=\"%s\" probability=\"%f\" />",entry.getKey().getID(),entry.getValue().doubleValue()));
        }
        
        writer.print("</strategy>");
        flush();

        return this;
    }
}
