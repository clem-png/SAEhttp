
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class TestLectureXml {

    //test unitaire de la classe LectureXml
    @Test
    public void testLectureXml() throws ParserConfigurationException, IOException, SAXException {
        LectureXml l = new LectureXml("config.xml");
        assertEquals(8080, l.port);
        assertEquals("source/", l.sD);
        assertEquals("racine/access.log", l.acceslog);
        assertEquals("racine/error.log", l.errorlog);
    }

}
