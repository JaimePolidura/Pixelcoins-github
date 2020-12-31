package es.serversurvival.socketWeb;

import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketWeb {
    public final static ServerSocketWeb INSTANCE = new ServerSocketWeb();

    private final String LOCALHOST = "127.0.0.1";
    private final int BUKKIT_PORT = 10000;
    private final int BACKEND_PORT = 9999;

    private ServerSocketWeb () {}

    public void enviarMensaje (SocketMessagge socketMessagge) {
        try{
            Socket serverSocket = new Socket(LOCALHOST, BACKEND_PORT);
            DataOutputStream output = new DataOutputStream(serverSocket.getOutputStream());
            output.writeUTF(socketMessagge.toString());

            output.close();
            serverSocket.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
