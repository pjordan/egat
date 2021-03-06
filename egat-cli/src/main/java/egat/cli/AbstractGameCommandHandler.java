/*
 * AbstractGameCommandHandler.java
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
package egat.cli;

import egat.cli.CommandProcessingException;
import egat.gamexml.SymmetricGameHandler;
import egat.gamexml.StrategicGameHandler;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.util.HelpFormatter;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Patrick Jordan
 */
public abstract class AbstractGameCommandHandler extends AbstractCommandHandler {
    private Option helpOption;
    private Option fileOption;
    private Option symmetricOption;
    private Group optionGroup;

    protected void addChildOptions(GroupBuilder groupBuilder) {
        groupBuilder.withName("options");
        groupBuilder.withOption(createFileOption());
        groupBuilder.withOption(createSymmetricOption());
        groupBuilder.withOption(createHelpOption());

        addAdditionalChildOptions(groupBuilder);
    }

    protected void addAdditionalChildOptions(GroupBuilder groupBuilder) {
    }

    protected Option getHelpOption() {
        return helpOption;
    }

    protected void setHelpOption(Option helpOption) {
        this.helpOption = helpOption;
    }

    protected Option createHelpOption() {
        DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();

        return helpOption = defaultOptionBuilder.withLongName("help")
                .withShortName("h")
                .withDescription("print help message for this command")
                .create();
    }

    protected Option getFileOption() {
        return fileOption;
    }


    protected Option createFileOption() {
        DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();
        ArgumentBuilder argumentBuilder = new ArgumentBuilder();

        return fileOption = defaultOptionBuilder.withLongName("file")
                .withShortName("f")
                .withDescription("game file")
                .withArgument(argumentBuilder.withMaximum(1)
                        .withMinimum(1)
                        .withName("file")
                        .create())
                .create();
    }

    protected Option createSymmetricOption() {
        DefaultOptionBuilder defaultOptionBuilder = new DefaultOptionBuilder();

        return symmetricOption = defaultOptionBuilder.withShortName("sym")
                .withLongName("symmetric")
                .withDescription("treat game as symmetric")
                .create();
    }

    protected Option getSymmetricOption() {
        return symmetricOption;
    }

    protected Group getOptionGroup() {
        return optionGroup;
    }

    protected void setOptionGroup(Group optionGroup) {
        this.optionGroup = optionGroup;
    }

    protected void printHelp() {
        HelpFormatter hf = new HelpFormatter();
        hf.setShellCommand("egat " + getCommandName());
        hf.setGroup(getCommand().getChildren());

        hf.getFullUsageSettings().add(DisplaySetting.DISPLAY_GROUP_NAME);
        hf.getFullUsageSettings().remove(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
        hf.getFullUsageSettings().remove(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        hf.getFullUsageSettings().remove(DisplaySetting.DISPLAY_OPTIONAL_CHILD_GROUP);
        hf.setFooter("egat is developed by Patrick R. Jordan (prjordan@umich.edu).");
        hf.print();
    }

    public void handleCommand(CommandLine commandLine) throws CommandProcessingException {

        if (commandLine.hasOption(getHelpOption())) {

            printHelp();

        } else {

            boolean symmetricFlag = commandLine.hasOption(getSymmetricOption());

            boolean fileFlag = commandLine.hasOption(getFileOption());

            InputStream inputStream = System.in;

            if (fileFlag) {

                String filename = (String) commandLine.getValue(getFileOption());

                try {

                    inputStream = new FileInputStream(filename);

                } catch (FileNotFoundException e) {

                    throw new CommandProcessingException(e);

                }

            }

            handleAdditionalChildOptions(commandLine);
            
            processCommand(inputStream, symmetricFlag);

        }

    }

    protected void handleAdditionalChildOptions(CommandLine commandLine) throws CommandProcessingException {
    }

    protected void processCommand(InputStream inputStream, boolean symmetric) throws CommandProcessingException {

        try {
            if (symmetric) {
                SAXParserFactory factory = SAXParserFactory.newInstance();

                SAXParser parser = factory.newSAXParser();

                SymmetricGameHandler handler = new SymmetricGameHandler();

                parser.parse(inputStream, handler);

                MutableSymmetricGame game = handler.getGame();

                processSymmetricGame(game);
            } else {

                SAXParserFactory factory = SAXParserFactory.newInstance();

                SAXParser parser = factory.newSAXParser();

                StrategicGameHandler handler = new StrategicGameHandler();

                parser.parse(inputStream, handler);

                MutableStrategicGame game = handler.getGame();

                processStrategicGame(game);
    
            }
        } catch (ParserConfigurationException e) {
            throw new CommandProcessingException(e);
        } catch (SAXException e) {
            throw new CommandProcessingException(e);
        } catch (IOException e) {
            throw new CommandProcessingException(e);
        }

    }

    protected abstract void processSymmetricGame(MutableSymmetricGame game) throws CommandProcessingException;

    protected abstract void processStrategicGame(MutableStrategicGame game) throws CommandProcessingException;
}
