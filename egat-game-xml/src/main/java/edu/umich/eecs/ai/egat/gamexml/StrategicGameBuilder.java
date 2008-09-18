/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.gamexml;

import edu.umich.eecs.ai.egat.game.Game;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Patrick Jordan
 */
public class StrategicGameBuilder {
    public Game buildGame(Component component) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(component) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String schema = getClass().getClassLoader().getResource("strategic.xsd").toString();
            SAXBuilder builder = new SAXBuilder();
            try {
                builder.setFeature("http://xml.org/sax/features/validation", true);
                builder.setFeature("http://apache.org/xml/features/validation/schema", true);
                builder.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
                builder.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", schema);
                builder.setErrorHandler(new ErrorHandler() {
                    public void warning(SAXParseException saxParseException) throws SAXException {
                        throw saxParseException;
                    }

                    public void error(SAXParseException saxParseException) throws SAXException {
                        throw saxParseException;
                    }

                    public void fatalError(SAXParseException saxParseException) throws SAXException {
                        throw saxParseException;
                    }
                });

                Document doc = builder.build(file);
                return XMLNfgFactory.buildStrategic(doc);
            } catch (JDOMException e) {
                JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea(e.getMessage(), 10, 30)), "The incorrect file schema...", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea(e.getMessage(), 10, 30)), "Error loading file...", JOptionPane.ERROR_MESSAGE);
            }
        }

        return null;
    }
}
