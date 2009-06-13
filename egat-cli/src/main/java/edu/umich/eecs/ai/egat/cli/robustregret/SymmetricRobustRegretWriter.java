/*
 * SymmetricRobustRegretWriter.java
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

        for (Action action : game.getActions()) {
            writeRegret(action, Games.robustRegret(action, game));
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
