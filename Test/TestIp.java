import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
public class TestIp {
    @Test
    public void testIsNotAccept_NON() throws ParserConfigurationException, IOException, SAXException {
        LectureXml lect = new LectureXml("config.xml");
        Ip ip =  new Ip(lect.ltAt, lect.lNAt);

        assertEquals( false, ip.IsNotAccept("192.168.0.0/24"));
    }

    @Test
    public void testIsNotAccept_OUI() throws ParserConfigurationException, IOException, SAXException {
        LectureXml lect = new LectureXml("config.xml");
        Ip ip =  new Ip(lect.ltAt, lect.lNAt);

        assertEquals( true, ip.IsNotAccept("192.168.3.0/24"));
    }

    @Test
    public void testIsNotAccept_OUIInconnu() throws ParserConfigurationException, IOException, SAXException {
        LectureXml lect = new LectureXml("config.xml");
        Ip ip =  new Ip(lect.ltAt, lect.lNAt);

        assertEquals( false, ip.IsNotAccept("191.168.0.0"));
    }

    @Test
    public void testIsAccept_AvecMasque() throws ParserConfigurationException, IOException, SAXException {
        LectureXml lect = new LectureXml("config.xml");
        Ip ip =  new Ip(lect.ltAt, lect.lNAt);

        assertEquals( true, ip.Masque("192.168.0.1"));

    }

    @Test
    public void testIsAccept_BonMasque_listNotAccept() throws ParserConfigurationException, IOException, SAXException {
        LectureXml lect = new LectureXml("config.xml");
        Ip ip =  new Ip(lect.ltAt, lect.lNAt);

        assertEquals(false, ip.Masque("192.168.1.0"));
    }

    @Test
    public void test_ipInconnu() throws ParserConfigurationException, IOException, SAXException {
        LectureXml lect = new LectureXml("config.xml");
        Ip ip =  new Ip(lect.ltAt, lect.lNAt);

        assertEquals(false, ip.Masque("191.168.0.0"));
    }

    @Test
    public void test_MasqueDe8() throws ParserConfigurationException, IOException, SAXException {
        LectureXml lect = new LectureXml("config.xml");
        Ip ip =  new Ip(lect.ltAt, lect.lNAt);

        assertEquals(true, ip.Masque("182.1.0.0"));
    }

    @Test
    public void test_MasqueDe16() throws ParserConfigurationException, IOException, SAXException {
        LectureXml lect = new LectureXml("config.xml");
        Ip ip =  new Ip(lect.ltAt, lect.lNAt);

        assertEquals(true, ip.Masque("172.14.1.0"));
    }

    @Test
    public void test_MasqueDe8_NON() throws ParserConfigurationException, IOException, SAXException {
        LectureXml lect = new LectureXml("config.xml");
        Ip ip =  new Ip(lect.ltAt, lect.lNAt);

        assertEquals(false, ip.Masque("192.0.0.0"));
    }

    @Test
    public void test_MasqueDe16_NON() throws ParserConfigurationException, IOException, SAXException {
        LectureXml lect = new LectureXml("config.xml");
        Ip ip =  new Ip(lect.ltAt, lect.lNAt);

        assertEquals(false, ip.Masque("172.1.14.0"));
    }


}
