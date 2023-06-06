import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.ArrayList;


public class HttpServer {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        ArrayList<String> listAccept = new ArrayList<String>();

        ArrayList<String> listNotAccept = new ArrayList<String>();



        //initialisation du serveur


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Chargement du fichier XML
        File fileConfi = new File("config.xml");
        Document document = builder.parse(fileConfi);

        Element root = document.getDocumentElement();

        // Récupération des valeurs des éléments
        NodeList nodeList = root.getElementsByTagName("port");
        int port = Integer.parseInt(nodeList.item(0).getTextContent());
        //System.out.println("Port : " + port);

        NodeList nodeList2 = root.getElementsByTagName("root");
        String pageDefaut = nodeList2.item(0).getTextContent();
        System.out.println("Page par défaut : " + pageDefaut);

        NodeList nodeList3 = root.getElementsByTagName("accept");
        for(int i = 0; i < nodeList3.getLength(); i++){
            listAccept.add(nodeList3.item(i).getTextContent());
            //System.out.println("ip accept : "+nodeList3.item(i).getTextContent());
        }

        NodeList nodeList4 = root.getElementsByTagName("reject");
        for(int i = 0; i < nodeList4.getLength(); i++){
            listNotAccept.add(nodeList4.item(i).getTextContent());
            //System.out.println("ip not accept"+nodeList4.item(i).getTextContent());
        }






        int portNumber = port;

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);


            System.out.println("Serveur HTTP démarré sur le port " + portNumber);

            while (true) {

                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                String request = in.readLine();
                System.out.println("Requête reçue : " + request);
                String[] requestParts = request.split(" ");

                if(!requestParts[0].equals("GET")){
                    out.write("requete incorrect \n \n -(!-!)- \n".getBytes());
                }

                String element = requestParts[1];

                if(element.equals("/")){
                    element = "index.html";
                }
                else{
                    element = element.substring(1);

                }

                if(element.endsWith(".html")){
                    File file = new File(element);
                    FileInputStream fis = new FileInputStream(file);
                    byte[] data = new byte[(int) file.length()];
                    fis.read(data);
                    fis.close();
                    out.write("HTTP/1.1 200 OK".getBytes());
                    out.write("Content-Type: text/html".getBytes());
                    String l = "Content-Length: " + data.length;
                    out.write(l.getBytes());
                    out.write(data);
                    out.flush();
                }else if (element.endsWith(".jpg") || element.endsWith(".jpeg") || element.endsWith(".png") || element.endsWith(".gif")){
                    try {
                        File file = new File(element);
                        FileInputStream fis = new FileInputStream(file);
                        byte[] data = new byte[(int) file.length()];
                        fis.read(data);
                        fis.close();
                        out.write("HTTP/1.1 200 OK".getBytes());
                        out.write("Content-Type: image/gif".getBytes());
                        String l = "Content-Length: " + data.length;
                        out.write(l.getBytes());
                        out.write(data);
                        out.flush();
                    }
                    catch (FileNotFoundException e) {
                        out.write("HTTP/1.1 404 Not Found".getBytes());
                        out.write("Content-Type: text/html".getBytes());
                        String l = "Content-Length: " + 0;
                        out.write(l.getBytes());
                        out.flush();
                    }

                }


                in.close();
                out.close();
                clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}