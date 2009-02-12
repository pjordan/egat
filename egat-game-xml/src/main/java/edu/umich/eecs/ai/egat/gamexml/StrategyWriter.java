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
