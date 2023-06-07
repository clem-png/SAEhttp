import java.util.ArrayList;

public class Ip {
   private ArrayList<String> listAccept = new ArrayList<String>();
   private ArrayList<String> listNotAccept = new ArrayList<String>();


    public Ip(ArrayList<String> listAccept){
        this.listAccept = listAccept;
    }

    public boolean IsAccept(String clientIp){
        for(int i = 0; i < listNotAccept.size(); i++){
            if(listNotAccept.get(i).equals(clientIp)){
                return false;
            }
        }
        for(int i = 0; i < listAccept.size(); i++){
            if(listAccept.get(i).equals(clientIp)){
                return true;
            }
        }
        return false;
    }

    public boolean Masque(String clientIp) {
        for (int i = 0; i < listAccept.size(); i++) {
            String tab1[] = listAccept.get(i).split("/");
            String tab2[] = tab1[0].split(".");
            String tab3[] = clientIp.split(".");

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
            } else if (tab1.length == 1) {
                if (tab2[0].equals(tab3[0]) && tab2[1].equals(tab3[1]) && tab2[2].equals(tab3[2]) && tab2[3].equals(tab3[3])) {
                    return true;
                }
            }
        }
        return false;
    }



}
