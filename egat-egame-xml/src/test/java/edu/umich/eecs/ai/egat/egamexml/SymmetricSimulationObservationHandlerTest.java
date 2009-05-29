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
