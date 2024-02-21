package sg.edu.nus.iss.baccarat.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerConnectionHandler implements Runnable {
    String args;

    public ServerConnectionHandler(String args) {
        this.args = args;
    }

    @Override
    public void run() {
            try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args))) {
                while (true) {
                    serverSocket.accept();
                }
    
            } catch (IOException io) {
                io.printStackTrace();
            }

    }
    
}
