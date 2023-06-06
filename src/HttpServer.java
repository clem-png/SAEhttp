import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;

public class HttpServer {
    public static void main(String[] args) {
        int portNumber = 8080;

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);


            System.out.println("Serveur HTTP démarré sur le port " + portNumber);

            while (true) {
                Socket clientSocket;
                clientSocket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream g = new DataOutputStream(clientSocket.getOutputStream());
                PrintWriter out = new PrintWriter(g, true);

                String request = in.readLine();

                System.out.println("Requête reçue : " + request);

                String[] requestParts = request.split(" ");
                String fileName = requestParts[1].substring(1);


                String fileContent = readFile(fileName);

                if (fileContent != null){
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/html");
                    out.println("Content-Length: " + fileContent.length());
                    out.println();
                    out.println(fileContent);
                } else {
                    out.println("HTTP/1.01 404 Not Found");
                    out.println();
                }

                in.close();
                out.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String fileName) {
        try {
            File file = new File(fileName);


            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder content = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    content.append(line);
                    content.append("\n");
                }

                reader.close();
                return content.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}