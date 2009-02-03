package edu.umich.eecs.ai.egat.egamexml;

import org.junit.Test;
import org.junit.Assert;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import java.io.IOException;

import edu.umich.eecs.ai.egat.egame.StrategicSimulationObserver;
import edu.umich.eecs.ai.egat.game.StrategicSimulation;

/**
 * @author Patrick Jordan
 */
public class StrategicSimulationObservationHandlerTest {
    @Test
    public void testLoad() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        StrategicSimulationObservationHandler handler = new StrategicSimulationObservationHandler();

        parser.parse( StrategicSimulationObservationHandlerTest.class.getResourceAsStream("/strategic.xml") , handler);

        Assert.assertNotNull(handler.getSimulation());

        StrategicSimulationObserver observer = handler.getObserver();

        Assert.assertEquals(observer.getObservationCount(),2L);
    }
}
