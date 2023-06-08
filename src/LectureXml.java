import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe permettant de lire le fichier de configuration XML
 */
public class LectureXml {
    ArrayList<String> ltAt = new ArrayList<String>();

    ArrayList<String> lNAt = new ArrayList<String>();

    String acceslog;

    String errorlog;

    String sD;

    int port;

    /**
     * Constructeur de la classe LectureXml
     * @param xml
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */

    public LectureXml(String xml) throws ParserConfigurationException, IOException, SAXException {
        ltAt = new ArrayList<String>();

        lNAt = new ArrayList<String>();



        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Chargement du fichier XML
        File fileConfi = new File(xml);
        Document document = builder.parse(fileConfi);

        Element root = document.getDocumentElement();

        // Récupération des valeurs des éléments
        NodeList nodeList = root.getElementsByTagName("port");
        port = Integer.parseInt(nodeList.item(0).getTextContent());

        NodeList nodeList2 = root.getElementsByTagName("root");
        sD = nodeList2.item(0).getTextContent();

        NodeList nodeList3 = root.getElementsByTagName("accept");
        for(int i = 0; i < nodeList3.getLength(); i++){
            ltAt.add(nodeList3.item(i).getTextContent());
            //System.out.println("ip accept : "+nodeList3.item(i).getTextContent());
        }

        NodeList nodeList4 = root.getElementsByTagName("reject");
        for(int i = 0; i < nodeList4.getLength(); i++){
            lNAt.add(nodeList4.item(i).getTextContent());
            //System.out.println("ip not accept"+nodeList4.item(i).getTextContent());
        }

        NodeList nodeList5 = root.getElementsByTagName("acceslog");
        acceslog = nodeList5.item(0).getTextContent();
        System.out.println("acceslog : " + acceslog);

        NodeList nodeList6 = root.getElementsByTagName("errorlog");
        errorlog = nodeList6.item(0).getTextContent();
        System.out.println("errorlog : " + errorlog);
    }

    // Getters

    public ArrayList<String> getLtAt() {
        return ltAt;
    }

    public ArrayList<String> getLNAt() {
        return lNAt;
    }

    public String getAcceslog() {
        return acceslog;
    }

    public String getErrorlog() {
        return errorlog;
    }

    public String getsD() {
        return sD;
    }

    public int getPort() {
        return port;
    }

}
