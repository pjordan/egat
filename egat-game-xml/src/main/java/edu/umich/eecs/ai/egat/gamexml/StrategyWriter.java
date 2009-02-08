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

    public StrategyWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public StrategyWriter(Writer writer) {
        this(new PrintWriter(writer));
    }

    public StrategyWriter(OutputStream outputStream) {
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

    public StrategyWriter write(Strategy strategy) throws IOException {

        writer.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writer.print("<strategy>");


        for(Map.Entry<Action,Number> entry : strategy.entrySet()) {
            writer.print(String.format("<action id=\"%s\" probability=\"%f\" />",entry.getKey().getID(),entry.getValue().doubleValue()));
        }
        
        writer.print("</strategy>");
        flush();

        return this;
    }
}
