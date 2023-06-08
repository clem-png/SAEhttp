import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Locale;

//bibliothèque pour python


public class HttpServer {

    private static String getStatus() throws IOException {

        //comme le bash ne marche, on utilise les fonctions java (trouver sur internet)pour avoir les infos

        Runtime lancement = Runtime.getRuntime();
        long memoireLibre = lancement.freeMemory();
        File disque = new File("/");
        long espaceDisqueLibre = disque.getFreeSpace();

        int nbProcess = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();


        /*
        Process p = Runtime.getRuntime().exec("ps aux | wc -l");
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String nbProcess = input.readLine();

        Process p2 = Runtime.getRuntime().exec("free -h");
        BufferedReader input2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
        String memoireLibre = input2.readLine();

        Process p3 = Runtime.getRuntime().exec("df -h");
        BufferedReader input3 = new BufferedReader(new InputStreamReader(p3.getInputStream()));
        String espaceDisqueLibre = input3.readLine();

        input2.close();
        input3.close();
        input.close();
         */



        String status = " Memoire libre:\n" +
                 memoireLibre +
                "\n Espace disque libre:\n" +
                 espaceDisqueLibre +
                "\n Nombre processus: " + nbProcess + "\n\n" ;
        //System.out.println(status);


        return status;
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, InterruptedException {

        //lecture du fichier xml

        LectureXml lectureXml = new LectureXml("config.xml");

        ArrayList<String> listAccept = lectureXml.ltAt;

        ArrayList<String> listNotAccept =  lectureXml.lNAt;

        String acceslog = lectureXml.acceslog;

        String errorlog = lectureXml.errorlog;

        String sourceDefaut = lectureXml.sD;

        int portNumber = lectureXml.port;



        //initialisation du serveur



        //fin initialisation du serveur
        String lastIp = "";

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);


            System.out.println("Serveur HTTP démarré sur le port " + portNumber);

            while (true) {

                Socket clientSocket = serverSocket.accept();
                String ip = clientSocket.getInetAddress().toString();
                ip = ip.substring(1);

                if(listAccept.contains(ip)){
                    //System.out.println("client accepté");
                }
                else if(listNotAccept.contains(ip)){
                    System.out.println("client refusé");

                    File fileErrorLog = new File(errorlog);
                    FileWriter fw = new FileWriter(fileErrorLog, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);
                    pw.println("clientRefusé ip: " + ip + " DD/MM/YYYY: " + java.time.LocalDate.now() + " HH:MM:SS: " + java.time.LocalTime.now());
                    pw.close();

                    clientSocket.close();
                    continue;
                }
                else{
                    System.out.println("client inconnu");
                    clientSocket.close();
                    continue;
                }

                if (!lastIp.equals(ip)){
                    lastIp = ip;
                    File fileAccesLog = new File(acceslog);
                    FileWriter fw = new FileWriter(fileAccesLog, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);
                    pw.println("clientConnecté ip: " + ip + " DD/MM/YYYY: " + java.time.LocalDate.now() + " HH:MM:SS: " + java.time.LocalTime.now());
                    pw.close();
                }

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
                    File file = new File(sourceDefaut+element);
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
                        File file = new File(sourceDefaut+element);
                        FileInputStream fis = new FileInputStream(file);

                        byte[] data = new byte[(int) file.length()];
                        byte[] data2 = new byte[(int) file.length()];
                        data2 = Base64.getEncoder().encode(data);

                        fis.read(data2);
                        fis.close();
                        out.write("HTTP/1.1 200 OK\r\n".getBytes());
                        out.write("Content-Type: image/jpeg\r\n".getBytes());
                        out.write("Content-Encoding: base64\r\n".getBytes());
                        out.write(("Content-Length: " + data2.length + "\r\n").getBytes());

                        out.write("\r\n".getBytes());

                        out.write(data2);
                        //System.out.println("image envoyé");
                        out.flush();
                    }

                    catch (FileNotFoundException e) {
                        out.write("HTTP/1.1 404 Not Found".getBytes());
                        out.write("Content-Type: text/html".getBytes());
                        String l = "Content-Length: " + 0;
                        out.write(l.getBytes());
                        out.flush();

                        File fileError = new File(errorlog);
                        FileWriter fw = new FileWriter(fileError, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter pw = new PrintWriter(bw);
                        pw.println("->clientConnecté ip: " + ip + " erreur: 404" + element + "DD/MM/YYYY: " + java.time.LocalDate.now() + " HH:MM:SS: " + java.time.LocalTime.now());
                        pw.close();
                    }

                }else if (element.equals("status")) {
                    String status = getStatus();
                    out.write("HTTP/1.1 200 OK\r\n".getBytes());
                    out.write("Content-Type: text/plain\r\n".getBytes());
                    String l = "Content-Length: " + status.length() + "\r\n";
                    out.write(l.getBytes());
                    out.write("\r\n".getBytes());
                    out.write(status.getBytes());
                    out.flush();
                } else if (element.equals("Dynamique")){
                    out.write("HTTP/1.1 200 OK\r\n".getBytes());
                    out.write("Content-Type: text/html\r\n".getBytes());

                    /*
                    ProcessBuilder pb = new ProcessBuilder("/usr/bin/python3", sourceDefaut+"date.py");
                    System.out.println("Dynamique");
                    Process p = pb.start();
                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;


                    p.waitFor();

                    ProcessBuilder pb2 = new ProcessBuilder("#!/bin/bash", sourceDefaut+"date.sh");
                    Process p2 = pb2.start();
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
                    String line2;
                    line2 = br2.readLine();


                    p2.waitFor();

                     */

                    LocalDateTime now = LocalDateTime.now();


                    String defaultDate = now.toString();



                    //écrire le code html
                    out.write(("<html>\n" +
                                                "\n" +
                                                "<head>\n" +
                                                "</head>\n" +
                                                "\n" +
                                                "<body>\n" +
                                                "<p> Bonjour </p>\n" +
                                                "<h1> Exemple avec la date </h1>\n" +
                                                "<h2>en bash</h2>\n" +
                                                "La date est <code interpreteur=«/bin/bash»>"+defaultDate+"</code>\n" +
                                                "\n" +
                                                "<h2>En python</h2>\n" +
                                                "\n" +
                                                "La date est <code interpreteur=«/usr/bin/python»>\n" +
                                                defaultDate +
                                                "</code>\n" +
                                                "\n" +
                                                "\n" +
                                                "</body>\n" +
                                                "\n" +
                                                "</html>".getBytes()).getBytes());
                    out.write("\r\n".getBytes());
                    out.flush();

                }
                in.close();
                out.close();
                clientSocket.close();
            }

        } catch (IOException e) {
            File fileError = new File(errorlog);
            FileWriter fw = new FileWriter(fileError, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println("->crach total");
            pw.close();

        }
    }
}