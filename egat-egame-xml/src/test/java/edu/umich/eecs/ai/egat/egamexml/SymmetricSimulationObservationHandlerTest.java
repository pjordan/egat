/*
 * SymmetricSimulationObservationHandlerTest.java
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
package edu.umich.eecs.ai.egat.egamexml;

import org.junit.Test;
import org.junit.Assert;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;

import edu.umich.eecs.ai.egat.game.SymmetricMultiAgentSystem;
import edu.umich.eecs.ai.egat.egame.SymmetricSimulationObserver;

import java.io.IOException;

/**
 * @author Patrick Jordan
 */
public class SymmetricSimulationObservationHandlerTest {
    @Test
    public void testLoad() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricSimulationObservationHandler handler = new SymmetricSimulationObservationHandler();


        parser.parse( SymmetricSimulationObservationHandlerTest.class.getResourceAsStream("/symmetric.xml") , handler);

        SymmetricMultiAgentSystem simulation = handler.getSimulation();
        SymmetricSimulationObserver observer = handler.getObserver();

        Assert.assertEquals(observer.getObservationCount(),2L);

        
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidLoad() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricSimulationObservationHandler handler = new SymmetricSimulationObservationHandler();

        parser.parse( SymmetricSimulationObservationHandlerTest.class.getResourceAsStream("/invalid-symmetric.xml") , handler);


    }

}
