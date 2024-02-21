package  sg.edu.nus.iss.baccarat.client;

import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws Exception {
        String[] stringSplit = args[0].split(":");
        String address = stringSplit[0];
        Integer port = Integer.parseInt(stringSplit[1]);
        
        try (Socket socket = new Socket(address, port)) {

        }
    }
}
