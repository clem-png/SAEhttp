import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Ip {
    //La liste des ips accepter et la liste des ips refusés
    private ArrayList<String> listAccept = new ArrayList<String>();
    private ArrayList<String> listNotAccept = new ArrayList<String>();


    /**
     * Constructeur de Ip
     *
     * @param listAccept
     * @param listNotAccept
     */
    public Ip(ArrayList<String> listAccept, ArrayList<String> listNotAccept) {
        this.listAccept = listAccept;
        this.listNotAccept = listNotAccept;
    }

    /**
     * @param clientIp Ip du client a tester
     * @return retourne true si l'ip de client se trouve dans la listAccept
     * et false si elle est dans la liste listNotAccpet
     */
    public boolean IsAccept(String clientIp) {
        for (int i = 0; i < listNotAccept.size(); i++) {
            if (listNotAccept.get(i).contains(clientIp)) {
                return false;
            }
        }
        for (int i = 0; i < listAccept.size(); i++) {
            if (listAccept.get(i).contains(clientIp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * la méthode fonctionne pour des ip IPv4 mais pas pour les ip IPv6
     *
     * @param clientIp L'ip du client
     * @return false si le masque n'est pas le bon true si il est bon
     */
    public boolean Masque(String clientIp) {
        //Cherche si l'ip est dans la bonne liste
        if (IsAccept(clientIp)) {
            for (int i = 0; i < listAccept.size(); i++) {
                if (listAccept.get(i).contains(":")) {
                    return true;
                } else {
                    //Si l'ip est en IPv4 on split le masque par / et split les parties par les points et on regarde manuellement
                    String tab1[] = listAccept.get(i).split("/");
                    String tab2[] = tab1[0].split("\\.");
                    String tab3[] = clientIp.split("\\.");
                    if (tab1.length == 2) {
                        if (tab1[1].equals("8")) {
                            if (tab2[0].equals(tab3[0])) {
                                return true;
                            }
                        }
                        if (tab1[1].equals("16")) {
                            if (tab2[0].equals(tab3[0]) && tab2[1].equals(tab3[1])) {
                                return true;
                            }
                        }
                        if (tab1[1].equals("24")) {
                            if (tab2[0].equals(tab3[0]) && tab2[1].equals(tab3[1]) && tab2[2].equals(tab3[2])) {
                                return true;
                            }
                        }
                    } else {
                        if (tab2[0].equals(tab3[0]) && tab2[1].equals(tab3[1]) && tab2[2].equals(tab3[2]) && tab2[3].equals(tab3[3])) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
