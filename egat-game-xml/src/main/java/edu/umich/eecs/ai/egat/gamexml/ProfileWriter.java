package edu.umich.eecs.ai.egat.gamexml;

import edu.umich.eecs.ai.egat.game.Strategy;
import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.Profile;
import edu.umich.eecs.ai.egat.game.Player;

import java.io.*;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public class ProfileWriter extends Writer {
    private PrintWriter writer;

    private boolean header;

    public ProfileWriter(PrintWriter writer) {
        this(writer, true);
    }

    public ProfileWriter(Writer writer) {
        this(new PrintWriter(writer));
    }

    public ProfileWriter(OutputStream outputStream) {
        this(new OutputStreamWriter(outputStream));
    }

    public ProfileWriter(PrintWriter writer, boolean header) {
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

    public ProfileWriter write(Profile profile) throws IOException {

        if(header) {
            writer.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        }
        
        writer.print("<profile>");

        for(Map.Entry<Player, Strategy> profileEntry : profile.entrySet()) {
            writer.print(String.format("<strategy player=\"%s\">",profileEntry.getKey().getID()));
            for(Map.Entry<Action,Number> entry : profileEntry.getValue().entrySet()) {
                writer.print(String.format("<action id=\"%s\" probability=\"%f\" />",entry.getKey().getID(),entry.getValue().doubleValue()));
            }
            writer.print("</strategy>");
        }
        writer.print("</profile>");
        flush();

        return this;
    }
}
