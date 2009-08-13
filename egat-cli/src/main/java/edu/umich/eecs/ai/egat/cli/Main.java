/*
 * Main.java
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
package edu.umich.eecs.ai.egat.cli;

import org.apache.commons.cli2.*;
import org.apache.commons.cli2.util.HelpFormatter;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;

import edu.umich.eecs.ai.egat.cli.regret.RegretCommandHandler;
import edu.umich.eecs.ai.egat.cli.ieds.IEDSCommandHandler;
import edu.umich.eecs.ai.egat.cli.replicatordynamics.ReplicatorDynamicsCommandHandler;
import edu.umich.eecs.ai.egat.cli.neresponse.NEResponseCommandHandler;
import edu.umich.eecs.ai.egat.cli.robustregret.RobustRegretCommandHandler;
import edu.umich.eecs.ai.egat.cli.strategyregret.StrategyRegretCommandHandler;
import edu.umich.eecs.ai.egat.cli.minform.MinFormCommandHandler;
import edu.umich.eecs.ai.egat.cli.eminform.EpsilonMinFormCommandHandler;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Patrick Jordan
 */
public class Main {
    public static void main(String[] args) {
        final DefaultOptionBuilder optionBuilder = new DefaultOptionBuilder();
        final GroupBuilder groupBuilder = new GroupBuilder();


        Option help = optionBuilder.withLongName("help")
                                   .withShortName("h")
                                   .withDescription("print this message")
                                   .create();
        Option version = optionBuilder.withLongName("version")
                                      .withShortName("v")
                                      .withDescription("print the version information and exit")
                                      .create();


        List<CommandHandler> commandHandlers = createCommandHandlers();

        groupBuilder.withName("command");
        for(CommandHandler handler : commandHandlers) {
            groupBuilder.withOption(handler.getCommand());
        }
        Group commandGroup = groupBuilder.create();


        Group mainOptions = groupBuilder.withName("options")
                                        .withOption(help)
                                        .withOption(version)
                                        .create();

        Group options = groupBuilder.withOption(mainOptions)
                                    .withOption(commandGroup)
                                    .create();

        Parser parser = new Parser();
        parser.setGroup(options);

        try {
            CommandLine cl = parser.parse(args);
            
            if (cl.hasOption(version)) {
                printVersion();
            } else {
                boolean commandTriggered = false;

                for(CommandHandler commandHandler : commandHandlers) {
                    if(cl.hasOption(commandHandler.getCommand())) {
                        commandHandler.handleCommand(cl);
                        commandTriggered = true;
                    }
                }

                if(!commandTriggered){
                    printHelp(options);
                }
            }

        } catch (OptionException e) {
            e.printStackTrace();
            printHelp(options);
        }
    }

    protected static void printHelp(Group options) {
        HelpFormatter hf = new HelpFormatter();
        hf.setShellCommand("egat");
        hf.setGroup(options);

        hf.getFullUsageSettings().add(DisplaySetting.DISPLAY_GROUP_NAME);        
        hf.getFullUsageSettings().remove(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
        hf.getFullUsageSettings().remove(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        hf.getFullUsageSettings().remove(DisplaySetting.DISPLAY_OPTIONAL_CHILD_GROUP);
        hf.setFooter("egat is developed by Patrick R. Jordan (prjordan@umich.edu).");
        hf.print();
    }

    protected static List<CommandHandler> createCommandHandlers() {
        List<CommandHandler> commandHandlers = new ArrayList<CommandHandler>();

        commandHandlers.add(new RegretCommandHandler());
        commandHandlers.add(new RobustRegretCommandHandler());
        commandHandlers.add(new IEDSCommandHandler());
        commandHandlers.add(new ReplicatorDynamicsCommandHandler());
        commandHandlers.add(new NEResponseCommandHandler());
        commandHandlers.add(new StrategyRegretCommandHandler());
        commandHandlers.add(new MinFormCommandHandler());
        commandHandlers.add(new EpsilonMinFormCommandHandler());
        return commandHandlers;
    }

    protected static void printVersion() {
        System.out.println("egat version 0.9");
    }
}
