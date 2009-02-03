package edu.umich.eecs.ai.egat.cli;

import org.apache.commons.cli2.option.Command;
import org.apache.commons.cli2.CommandLine;

/**
 * @author Patrick Jordan
 */
public interface CommandHandler {
    Command getCommand();

    void handleCommand(CommandLine commandLine) throws RuntimeException;
}
